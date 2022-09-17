package top.yqingyu.common.nio$server.event$demo.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.nio$server.core.EventHandler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

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

    public DemoEventHandler(Selector selector) {
        super(selector);
    }

    /**
     * 单次加载资源
     *
     * @author YYJ
     * @description
     */
    @Override
    public void loading() {

    }

    /**
     * @param selector
     * @param socketChannel
     * @throws IOException
     */
    @Override
    public void read(Selector selector,SocketChannel socketChannel) throws IOException {
        log.info("HELLO WORLD! - ++");
        socketChannel.register(selector, SelectionKey.OP_WRITE);
    }

    /**
     * @param selector
     * @param socketChannel
     * @throws IOException
     */
    @Override
    public void write(Selector selector, SocketChannel socketChannel) throws IOException {
        log.info("HELLO WORLD! - ==");
        socketChannel.close();
    }

    /**
     * @param selector
     * @param socketChannel
     * @throws IOException
     */
    @Override
    public void assess(Selector selector, SocketChannel socketChannel) throws IOException, InterruptedException {
            while (true){
                Object take = QUEUE.take();

            }
    }
}
