package top.yqingyu.common.nio$server.event$http.server;

import org.apache.commons.lang3.StringUtils;
import top.yqingyu.common.nio$server.CreateServer;
import top.yqingyu.common.nio$server.event$http.compoment.HttpEventHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$http.server.HttpServer
 * @description  简易Http服务
 * @createTime 2022年09月09日 20:35:00
 */
public class HttpServerStarter {

    public static final String SERVER_NAME;

    static {
        String temp;

        temp = System.getProperty("server.name");
        if (StringUtils.isBlank(temp)) {
            temp = "QyHttp";
        }
        SERVER_NAME = temp;
    }

    public static void start(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        CreateServer
                .createDefault(SERVER_NAME)
                .implEvent(HttpEventHandler.class)
                .loadingEventResource()
                .defaultFixRouter(HttpEventHandler.handlerNumber,HttpEventHandler.perHandlerWorker)
                .listenPort(HttpEventHandler.port)
                .start();
    }
}
