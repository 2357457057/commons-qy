package top.yqingyu.common.nio$server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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

    protected final OperatingRecorder<Integer> OPERATE_RECORDER = new OperatingRecorder<>(4096);

    protected final ConcurrentHashMap<Integer, SocketChannel> SocketChannels = new ConcurrentHashMap<>();

    /**
     * 是否正在重建当前 selector
     */
    protected final AtomicBoolean IS_REBUILDING = new AtomicBoolean(false);

    protected final LinkedBlockingQueue<Object> QUEUE = new LinkedBlockingQueue<>();


    public EventHandler(Selector selector) {
        this.selector = selector;
    }

    public EventHandler() {
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

                while (iterator.hasNext()) {

                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    SocketChannel channel = (SocketChannel) selectionKey.channel();

                    int i = channel.hashCode();

                    if (selectionKey.isReadable()) {
                        read(selector, channel);
                        //发现多线程时同一个selectKey会空读轮询 此处添加hash简单排除，以防处理异常
                    } else if (selectionKey.isWritable() && OPERATE_RECORDER.add(i)) {
                        write(selector, channel);
                    }
                }

            } catch (ExceedingRepetitionLimitException e) {
                log.warn("发现疑似空循环 {}",e.getMessage());
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

    public abstract void read(Selector selector, SocketChannel socketChannel) throws Exception;

    public abstract void write(Selector selector, SocketChannel socketChannel) throws Exception;

    public abstract void assess(Selector selector, SocketChannel socketChannel) throws Exception;


    private void rebuildSelector() throws IOException {
        IS_REBUILDING.setRelease(true);

        OPERATE_RECORDER.clear();

        this.selector = Selector.open();
        SocketChannels.forEach((I, S) -> {
            try {
                if (S.isOpen()) S.register(this.selector, SelectionKey.OP_READ);
                else SocketChannels.remove(I);
            } catch (Exception e) {
                log.error("selector 重建异常", e);
            }
        });
        IS_REBUILDING.setRelease(false);
        log.info("Selector重构完成");
    }
}
