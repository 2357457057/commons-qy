package top.yqingyu.common.nio$server.event;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.event.abs.EventHandler
 * @description
 * @createTime 2022年09月07日 15:58:00
 */
@Slf4j
public abstract class EventHandler implements Runnable {
    private final Selector selector;
    protected final ThreadPoolExecutor POOL;

    public EventHandler(Selector selector, ThreadPoolExecutor pool) {
        this.selector = selector;
        POOL = pool;
    }

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

                    if (selectionKey.isReadable()) {
                        read(selector, selectionKey);
                    } else if (selectionKey.isWritable()) {
                        write(selector, selectionKey);
                    }
                }

            } catch (Exception e) {
                log.error("服务异常", e);
            }

        }
    }

    public abstract void read(Selector selector, SelectionKey selectionKey) throws IOException;

    public abstract void write(Selector selector, SelectionKey selectionKey) throws IOException;

}
