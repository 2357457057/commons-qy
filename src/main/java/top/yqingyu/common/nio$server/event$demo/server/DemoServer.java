package top.yqingyu.common.nio$server.event$demo.server;

import cn.hutool.core.date.LocalDateTimeUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.Selector;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.DemoServer
 * @description
 * @createTime 2022年09月08日 02:13:00
 */
public class DemoServer {
    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 100; i++) {
            Selector open = Selector.open();
            open.selectNow();
        }

        System.out.println(LocalDateTimeUtil.between(now,LocalDateTime.now(), ChronoUnit.MILLIS));
    }
}
