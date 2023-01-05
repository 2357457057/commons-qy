package top.yqingyu.common.qymsg;


import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.utils.IoUtil;
import top.yqingyu.common.utils.LocalDateTimeUtil;
import top.yqingyu.common.utils.ThreadUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import static top.yqingyu.common.qymsg.Dict.*;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.qymsg.MsgHelper
 * @description
 * @createTime 2022年09月02日 00:31:00
 */

public class MsgHelper implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(MsgHelper.class);

    public static String gainMsg(QyMsg msg) {
        return msg.getDataMap().getString(QYMSG, "");
    }

    public static Object gainObjMsg(QyMsg msg) {
        return msg.getDataMap().get(QYMSG);
    }

    public static String gainMsgValue(QyMsg msg, String key) {
        return msg.getDataMap().getString(key, "");
    }

    public static Object gainMsgOBJ(QyMsg msg, String key) {
        return msg.getDataMap().get(key);
    }

    //     * 文件唯一ID
//     * 文件名
//     * 文件总长度
//     * 切分次数
//     * 切分长度
    public static List<QyMsg> buildFileMsg(File file, int transThread, String remotePath) throws CloneNotSupportedException {
        if (!(transThread > 0 && transThread <= TRANS_THREAD_MAX))
            throw new IllegalArgumentException("线程数不对！");

        long fileLength = file.length();
        long CUT_LENGTH = fileLength / transThread;
        long yu = fileLength % transThread;

        QyMsg qyMsg = new QyMsg(MsgType.NORM_MSG, DataType.FILE);
        qyMsg.putMsgData(FILE_ID, UUID.fastUUID().toString());
        qyMsg.putMsgData(FILE_NAME, file.getName());
        qyMsg.putMsgData(FILE_LENGTH, fileLength);

        qyMsg.putMsgData(FILE_LOCAL_PATH, file.getPath());
        qyMsg.putMsgData(FILE_REMOTE_PATH, remotePath);
        qyMsg.putMsgData(FILE_CUT_TIMES, transThread);

        ArrayList<QyMsg> msg = new ArrayList<>();

        if (transThread != 1) {
            for (int i = 1; i <= transThread; i++) {
                QyMsg clone = qyMsg.clone();
                msg.add(clone);
                clone.putMsgData(FILE_IDX, i);
                clone.putMsgData(FILE_POSITION, Math.max((i - 1) * CUT_LENGTH - 1, 0));
                clone.putMsgData(FILE_CUT_LENGTH, transThread - 1 == i ? yu : CUT_LENGTH);
            }
            return msg;
        }

        qyMsg.putMsgData(FILE_POSITION, 0);
        msg.add(qyMsg);
        return msg;
    }


    private final BlockingQueue<QyMsg> inQueue;
    private final BlockingQueue<QyMsg> outQueue;

    private final AtomicBoolean running;
    private final int clearTime;
    private final HashMap<String, ArrayList<QyMsg>> MSG_CONTAINER = new HashMap<>();

    private final ReentrantLock lock = new ReentrantLock();

    public MsgHelper(BlockingQueue<QyMsg> inQueue, BlockingQueue<QyMsg> outQueue, AtomicBoolean running, int clearTime) {
        this.inQueue = inQueue;
        this.outQueue = outQueue;
        this.running = running;
        this.clearTime = clearTime;
    }

    /**
     * @param inQueue  输入队列
     * @param outQueue 输出队列
     * @param thName   线程名称
     * @param running  运行
     * @author YYJ
     * @description 初始化消息组装类
     */
    public static void init(BlockingQueue<QyMsg> inQueue, BlockingQueue<QyMsg> outQueue, String thName, AtomicBoolean running, int clearTime) {
        Thread thread = new Thread(new MsgHelper(inQueue, outQueue, running, clearTime));
        thread.setName(thName);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {

        String monitor = Thread.currentThread().getName() + "monitor";


        ThreadUtil.createPeriodScheduled(0, 30, TimeUnit.MINUTES, () -> {
            ThreadUtil.setThisThreadName(monitor);
            try {
                lock.lock();
                LocalDateTime now = LocalDateTime.now();
                MSG_CONTAINER.forEach((k, list) -> {
                    Optional<QyMsg> max =
                            list.stream()
                                    .max((o1, o2) ->
                                            (int) LocalDateTimeUtil.between(
                                                    (LocalDateTime) MsgHelper.gainMsgOBJ(o1, "now"),
                                                    (LocalDateTime) MsgHelper.gainMsgOBJ(o2, "now"),
                                                    ChronoUnit.SECONDS)
                                    );

                    if (max.isPresent()) {

                        //当前分区的数据最老数据
                        QyMsg maxMsg = max.get();
                        long min = LocalDateTimeUtil.between(now, (LocalDateTime) MsgHelper.gainMsgOBJ(maxMsg, "now"), ChronoUnit.MINUTES);
                        if (min > clearTime) {
                            MSG_CONTAINER.remove(k);
                            log.debug("消息过期，已清除 {} ", maxMsg);
                        }

                    }
                });
            } catch (Exception e) {

                log.error("容器清除器异常", e);

            } finally {
                lock.unlock();
            }

        });
        while (running.get()) {
            try {
                lock.lock();
                QyMsg take = inQueue.take();
                String partition_id = take.getPartition_id();
                Integer denominator = take.getDenominator();
                ArrayList<QyMsg> list = MSG_CONTAINER.get(partition_id);

                //最后一块拼图。
                if (list != null && list.size() + 1 == denominator) {
                    list.add(take);
                    byte[] b = new byte[0];
                    AtomicReference<byte[]> buf = new AtomicReference<>();
                    buf.set(b);
                    list
                            .stream()
                            .sorted(Comparator.comparingInt(QyMsg::getNumerator))
                            .forEach(a ->
                                    buf.set(
                                            ArrayUtils.addAll(buf.get(), (byte[]) MsgHelper.gainObjMsg(a))
                                    )
                            );

                    MsgType msgType = take.getMsgType();
                    DataType dataType = take.getDataType();

                    QyMsg out = new QyMsg(msgType, dataType);
                    out.setSegmentation(false);
                    out.setFrom(take.getFrom());

                    if (DataType.JSON.equals(dataType)) {

                        out = JSON.parseObject(buf.get(), QyMsg.class);

                    } else if (DataType.OBJECT.equals(dataType)) {

                        out = IoUtil.deserializationObj(buf.get(), QyMsg.class);

                    } else if (DataType.STRING.equals(dataType)) {

                        String s = new String(buf.get(), StandardCharsets.UTF_8);
                        out.putMsg(s);
                    } else if (DataType.STREAM.equals(dataType)) {
                        out.putMsg(buf.get());
                    } else {
                        out.putMsg(buf.get());
                    }


                    outQueue.put(out);
                    MSG_CONTAINER.remove(partition_id);
                    log.debug("消息partition {} 拼接完成", partition_id);

                } else if (list != null && MSG_CONTAINER.get(partition_id).size() + 1 != denominator) {
                    take.putMsgData("now", LocalDateTime.now());
                    MSG_CONTAINER.get(partition_id).add(take);
                } else {

                    ArrayList<QyMsg> qyMsgArrayList = new ArrayList<>();
                    qyMsgArrayList.add(take);
                    MSG_CONTAINER.put(partition_id, qyMsgArrayList);
                }


            } catch (Exception e) {
                log.debug("消息组装异常", e);
            } finally {
                lock.unlock();
            }
        }

    }
}
