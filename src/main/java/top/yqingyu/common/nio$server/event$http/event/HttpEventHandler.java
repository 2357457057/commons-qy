package top.yqingyu.common.nio$server.event$http.event;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.nio$server.core.EventHandler;
import top.yqingyu.common.nio$server.event$http.compoment.LocationMapping;
import top.yqingyu.common.nio$server.event$http.route.SuperRoute;
import top.yqingyu.common.utils.IoUtil;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.event$handler.HttpEventHandler
 * @description
 * @createTime 2022年09月09日 18:05:00
 */

public class HttpEventHandler extends EventHandler {
    public HttpEventHandler() {
        super();
    }

    public HttpEventHandler(Selector selector, ThreadPoolExecutor pool) {
        super(selector, pool);
    }

    /**
     * 加载URL
     *
     * @author YYJ
     * @description
     */
    @Override
    public void loading() {
        String path = "C:\\Users\\Yangy\\OneDrive\\桌面\\webapps\\ROOT";
        LocationMapping.loadingFileResource(path);
        log.info("localResourcePath: {}", path);
    }

    private static final Logger log = LoggerFactory.getLogger(HttpEventHandler.class);

    @Override
    public void read(Selector selector, SelectionKey selectionKey) throws Exception {

        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        new SuperRoute().superServlet(socketChannel);
        socketChannel.close();
    }


    @Override
    public void write(Selector selector, SelectionKey selectionKey) throws Exception {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        IoUtil.writeBytes(socketChannel, ("HTTP/1.1 200 OK\r\n" +
                "Content-Type:application/json\r\n" +
                "\r\n" +
                "{}").getBytes(StandardCharsets.UTF_8));
        socketChannel.register(selector, SelectionKey.OP_READ);

        socketChannel.close();
    }


    @Override
    public void assess(Selector selector, SelectionKey selectionKey) throws Exception {

        while (true) {
            try {
                Object take = QUEUE.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                write(selector, selectionKey);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }

    }


}
