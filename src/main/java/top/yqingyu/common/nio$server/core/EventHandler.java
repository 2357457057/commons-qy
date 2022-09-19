package top.yqingyu.common.nio$server.core;

import cn.hutool.core.collection.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

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
    protected ThreadPoolExecutor POOL;

    protected final ConcurrentHashSet<Integer> SINGLE_OPS = new ConcurrentHashSet<>();
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
                    //发现多线程时同一个selectKey会空读轮询 此处添加hash简单排除，以防处理异常
                    if (selectionKey.isValid()) {
                        if (selectionKey.isReadable()) {
                            read(selector, channel);
                        } else if (selectionKey.isWritable() && SINGLE_OPS.add(i)) {
                            write(selector, channel);
                        }
                    }
                }

            } catch (Exception e) {
                log.error("服务异常", e);
            }

        }
    }

    public abstract void read(Selector selector, SocketChannel socketChannel) throws Exception;

    public abstract void write(Selector selector, SocketChannel socketChannel) throws Exception;

    public abstract void assess(Selector selector, SocketChannel socketChannel) throws Exception;
}
