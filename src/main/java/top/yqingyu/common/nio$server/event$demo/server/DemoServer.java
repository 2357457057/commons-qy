package top.yqingyu.common.nio$server.event$demo.server;

import top.yqingyu.common.nio$server.CreateServer;
import top.yqingyu.common.nio$server.event$demo.event.DemoEventHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.DemoServer
 * @description
 * @createTime 2022年09月08日 02:13:00
 */
public class DemoServer {
    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        CreateServer
                .createDefault(45321, "demo")
                .implEvent(DemoEventHandler.class)
                .defaultFixRouter(4, 2)
                .listenPort(0)
                .start();
    }
}
