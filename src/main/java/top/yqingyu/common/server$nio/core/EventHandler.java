package top.yqingyu.common.server$nio.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.bean.NetChannel;
import top.yqingyu.common.qydata.ConcurrentQyMap;
import top.yqingyu.common.utils.Status;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.event.abs.EventHandler
 * @description
 * @createTime 2022年09月07日 15:58:00
 */

public abstract class EventHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(EventHandler.class);
    private Selector selector;
    protected ThreadPoolExecutor READ_POOL;
    protected ThreadPoolExecutor WRITE_POOL;

    protected static final OperatingRecorder<Integer> OPERATE_RECORDER = OperatingRecorder.createNormalRecorder(1024L * 1024);

    protected final ConcurrentQyMap<Integer, ConcurrentQyMap<String, Object>> NET_CHANNELS = new ConcurrentQyMap<>();

    /**
     * 是否正在重建当前 selector
     */
    protected final AtomicBoolean IS_REBUILDING = new AtomicBoolean(false);

    protected final LinkedBlockingQueue<Object> QUEUE = new LinkedBlockingQueue<>();


    public EventHandler(Selector selector) throws IOException {
        this.selector = selector;
    }

    public EventHandler() throws IOException {
    }

    /**
     * 单次加载资源
     *
     * @author YYJ
     * @description
     */
    protected abstract void loading();

    public Selector getSelector() {
        return selector;
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            try {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                if (keys.isEmpty())
                    OPERATE_RECORDER.add2(selector.hashCode());

                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ConcurrentQyMap<String, Object> status = NET_CHANNELS.get(channel.hashCode());

                    if (status == null) {
                        NetChannel netChannel = new NetChannel(channel);
                        status = new ConcurrentQyMap<String, Object>()
                                .putConsecutive("NetChannel", netChannel)
                                .putConsecutive("LocalDateTime", LocalDateTime.now())
                                .putConsecutive(ChannelStatus.WRITE, Boolean.FALSE)
                                .putConsecutive(ChannelStatus.READ, Boolean.FALSE);
                        NET_CHANNELS.put(netChannel.hashCode(), status);
                    }
                    if (selectionKey.isReadable()) {
                        if (Status.canDo(status, ChannelStatus.READ)) {
                            Status.statusTrue(status, ChannelStatus.READ);
                            read(selector, new NetChannel(channel));
                        }
                    } else {
                        if (Status.canDo(status, ChannelStatus.WRITE)) {
                            Status.statusTrue(status, ChannelStatus.WRITE);
                            write(selector, new NetChannel(channel));
                        }
                    }
                    if (status == null) {
                        log.warn("{} is null", channel.hashCode());
                        selectionKey.cancel();
                    }

                }

            } catch (RebuildSelectorException e) {
                log.warn("触发rebuild {}", e.getMessage());
                try {
                    rebuildSelector();
                } catch (IOException ex) {
                    log.error("新Selector构建失败 ", ex);
                }
            } catch (Exception e) {
                log.error("服务异常", e);
            }

        }
    }

    public abstract void read(Selector selector, NetChannel socketChannel) throws Exception;

    public abstract void write(Selector selector, NetChannel socketChannel) throws Exception;

    public abstract void assess(Selector selector, NetChannel socketChannel) throws Exception;


    private void rebuildSelector() throws IOException {
        IS_REBUILDING.setRelease(true);

        OPERATE_RECORDER.remove(this.selector.hashCode());
        this.selector.close();
        this.selector = Selector.open();
        NET_CHANNELS.forEach((I, S) -> {
            try {
                if (S.get("NetChannel", NetChannel.class).isOpen() && S.get("NetChannel", NetChannel.class).isConnectionPending())
                    S.get("NetChannel", NetChannel.class).register(this.selector, SelectionKey.OP_READ);
                else NET_CHANNELS.remove(I);
            } catch (Exception e) {
                log.error("selector 重建异常", e);
            }
        });
        IS_REBUILDING.setRelease(false);
        this.selector.wakeup();
        log.info("Selector重构完成");
    }
}
