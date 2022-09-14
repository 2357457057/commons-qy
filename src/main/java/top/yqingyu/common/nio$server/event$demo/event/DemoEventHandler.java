package top.yqingyu.common.nio$server.event$demo.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.nio$server.core.EventHandler;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$demo.event.DemoEventHandler
 * @description
 * @createTime 2022年09月09日 20:37:00
 */
public class DemoEventHandler extends EventHandler {

    public DemoEventHandler() {
        super();
    }

    private static final Logger log = LoggerFactory.getLogger(DemoEventHandler.class);

    public DemoEventHandler(Selector selector, ThreadPoolExecutor pool) {
        super(selector, pool);
    }

    /**
     * 单次加载资源
     *
     * @param obj 资源
     * @author YYJ
     * @description
     */
    @Override
    public void loading() {

    }

    /**
     * @param selector
     * @param selectionKey
     * @throws IOException
     */
    @Override
    public void read(Selector selector, SelectionKey selectionKey) throws IOException {
        log.info("HELLO WORLD! - ++");
        SelectableChannel channel = selectionKey.channel();
        channel.register(selector, SelectionKey.OP_WRITE);
    }

    /**
     * @param selector
     * @param selectionKey
     * @throws IOException
     */
    @Override
    public void write(Selector selector, SelectionKey selectionKey) throws IOException {
        log.info("HELLO WORLD! - ==");
        SelectableChannel channel = selectionKey.channel();
        channel.close();
    }

    /**
     * @param selector
     * @param selectionKey
     * @throws IOException
     */
    @Override
    public void assess(Selector selector, SelectionKey selectionKey) throws IOException, InterruptedException {
            while (true){
                Object take = QUEUE.take();

            }
    }
}
