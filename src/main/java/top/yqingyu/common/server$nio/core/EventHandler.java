package top.yqingyu.common.server$nio.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.bean.NetChannel;
import top.yqingyu.common.qydata.ConcurrentQyMap;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
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

    protected final ConcurrentQyMap<Integer, ConcurrentQyMap<String, Object>> SOCKET_CHANNELS = new ConcurrentQyMap<>();

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

                if (keys.size() == 0)
                    OPERATE_RECORDER.add2(selector.hashCode());

                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    SocketChannel channel = (SocketChannel) selectionKey.channel();

                    if (selectionKey.isReadable()) {
                        read(selector,new NetChannel(channel));
                    } else {
                        write(selector, new NetChannel(channel));
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

        this.selector = Selector.open();
        SOCKET_CHANNELS.forEach((I, S) -> {
            try {
                if (S.get("SocketChannel", SocketChannel.class).isOpen() && S.get("SocketChannel", SocketChannel.class).isConnectionPending())
                    S.get("SocketChannel", SocketChannel.class).register(this.selector, SelectionKey.OP_READ);
                else SOCKET_CHANNELS.remove(I);
            } catch (Exception e) {
                log.error("selector 重建异常", e);
            }
        });
        IS_REBUILDING.setRelease(false);
        log.info("Selector重构完成");
    }
}
