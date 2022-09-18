package top.yqingyu.common.nio$server.event$http.compoment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.nio$server.core.EventHandler;
import top.yqingyu.common.qydata.DataMap;
import top.yqingyu.common.utils.IoUtil;
import top.yqingyu.common.utils.UnitUtil;
import top.yqingyu.common.utils.YamlUtil;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static top.yqingyu.common.nio$server.event$http.compoment.SuperRoute.*;

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
        DataMap cfg = yamlUtil.getNotNUllData("server-cfg.yml");
        {

            DataMap server = cfg.getNotNUllData("server");
            {
                port = server.getIntValue("port", 4732);
                handlerNumber = server.getIntValue("handler-num", 4);
                perHandlerWorker = server.getIntValue("per-worker-num", 4);
                Long workerKeepLiveTime = server.$2MILLS("worker-keep-live-time", UnitUtil.$2MILLS("2H"));
                boolean open_resource = server.getBooleanValue("open-resource", true);
                boolean open_controller = server.getBooleanValue("open-controller", true);
                String path = server.getString("local-resource-path", System.getProperty("user.dir"));
                String scan_package = server.getString("controller-package", "top.yqingyu.common.web.controller");

                if (open_resource) LocationMapping.loadingBeanResource(scan_package);
                if (open_controller) LocationMapping.loadingFileResource(path);

                log.info("localResourcePath: {}", path);
            }

            DataMap transfer = cfg.getNotNUllData("transfer");
            {
                DataMap request = transfer.getNotNUllData("request");
                {
                    DEFAULT_BUF_LENGTH = request.$2B("parse-buffer-size", UnitUtil.$2B("1KB"));
                    MAX_HEADER_SIZE = request.$2B("max-header-size",UnitUtil.$2B("64KB") );
                    MAX_BODY_SIZE = request.$2B("max-body-size", UnitUtil.$2B("128MB"));
                }
                DataMap response = transfer.getNotNUllData("response");
                {
                    DEFAULT_SEND_BUF_LENGTH = response.$2B("send-buf-size", UnitUtil.$2B("2MB"));
                }
            }
            DataMap file_compress = cfg.getNotNUllData("file-compress");
            {
                FILE_COMPRESS_ON = file_compress.getBoolean("open",true);
                MAX_SINGLE_FILE_COMPRESS_SIZE = file_compress.$2B("max-single-file-compress-size", UnitUtil.$2B("128MB"));
                DataMap compressPool = file_compress.getData("compress-cache-pool");
                {
                    MAX_FILE_CACHE_SIZE = compressPool.$2B("max-file-cache-size", UnitUtil.$2B("0.5GB"));
                    CACHE_POOL_ON = compressPool.getBoolean("open",true);
                }
            }

            DataMap session = cfg.getNotNUllData("session");
            Long aLong = session.$2S("session-timeout", UnitUtil.$2S("7D"));

        }
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
