package top.yqingyu.common.nio$server.event$http.server;

import top.yqingyu.common.nio$server.CreateServer;
import top.yqingyu.common.nio$server.event$http.event.HttpEventHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$http.server.HttpServer
 * @description  简易Http服务
 * @createTime 2022年09月09日 20:35:00
 */
public class HttpServer {
    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        CreateServer
                .createDefault("Http-Qy")
                .implEvent(HttpEventHandler.class)
                .loadingEventResource()
                .defaultFixRouter(32)
                .listenPort(6453)
                .start();
    }
}
