package top.yqingyu.common.nio$server;

import top.yqingyu.common.nio$server.event.EventHandler;

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
    private void demo() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        CreateServer
                .createDefault(8080, "main")
                .implEvent(EventHandler.class)
                .defaultFixRouter(4,2)
                .listenPort(0)
                .start();
    }
}
