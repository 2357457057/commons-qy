package top.yqingyu.common.nio$server.event$http.compoment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.nio$server.core.EventHandler;
import top.yqingyu.common.qydata.DataMap;
import top.yqingyu.common.utils.IoUtil;
import top.yqingyu.common.utils.YamlUtil;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

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

    public static int port;
    public static int handlerNumber;
    public static int perHandlerWorker;

    public HttpEventHandler(Selector selector) {
        super(selector);
    }

    /**
     * 加载URL
     *
     * @author YYJ
     * @description
     */
    @Override
    protected void loading() {

        DataMap yamlUtil = YamlUtil.loadYaml("server-cfg", YamlUtil.LoadType.BOTH).getCfgData();
        DataMap cfg = yamlUtil.getDataMap("server-cfg.yml");
        DataMap server = cfg.getDataMap("server");


        String path = server.getString("local-resource-path", System.getProperty("user.dir"));
        String scan_package = server.getString("controller-package", "top.yqingyu.common.web.controller");
        port = server.getIntValue("port", 4732);
        handlerNumber = server.getIntValue("handler-num", 4);
        perHandlerWorker = server.getIntValue("per-worker-num", 4);

        LocationMapping.loadingFileResource(path);
        if (!server.getBooleanValue("only-resource", true))
            LocationMapping.loadingBeanResource(scan_package);
        log.info("localResourcePath: {}", path);
    }

    private static final Logger log = LoggerFactory.getLogger(HttpEventHandler.class);

    @Override
    @SuppressWarnings("unchecked")
    public void read(Selector selector, SocketChannel socketChannel) throws Exception {
        POOL.submit(new SuperRoute(socketChannel, SINGLE_OPS));
    }


    @Override
    public void write(Selector selector, SocketChannel socketChannel) throws Exception {

        IoUtil.writeBytes(socketChannel, ("HTTP/1.1 200 OK\r\n" +
                "Content-Type:application/json\r\n" +
                "\r\n" +
                "{}").getBytes(StandardCharsets.UTF_8));
        socketChannel.register(selector, SelectionKey.OP_READ);
        socketChannel.close();
    }


    @Override
    public void assess(Selector selector, SocketChannel socketChannel) throws Exception {
    }


}
