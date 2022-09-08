package top.yqingyu.common.nio$server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.nio$server.event.EventHandler;
import top.yqingyu.common.utils.ThreadUtil;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.core.HandlerHolder
 * @description
 * @createTime 2022年09月07日 13:42:00
 */

public class HandlerHolder {

    private static final Logger log = LoggerFactory.getLogger(HandlerHolder.class);

    private final ArrayList<EventHandler> list;
    private final int size;
    private final AtomicInteger IDX = new AtomicInteger();

    public final HashSet<EventHandler> RUNNER = new HashSet<>();


    private HandlerHolder(int size, int perHandlerPoolSize, Class<? extends EventHandler> clazz) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.size = size;
        this.list = new ArrayList<>(size);
        Constructor<? extends EventHandler> constructor = clazz.getConstructor(Selector.class, ThreadPoolExecutor.class);
        for (int i = 0; i < size; i++) {
            list.add(constructor.newInstance(Selector.open(), ThreadUtil.createQyFixedThreadPool(perHandlerPoolSize, null, null)));
        }
    }

    public static HandlerHolder createDefaultSize(Class<? extends EventHandler> clazz) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        int i = Runtime.getRuntime().availableProcessors();
        return new HandlerHolder(i * 2, 2, clazz);
    }

    public static HandlerHolder createFixed(int size, int perHandlerPoolSize, Class<? extends EventHandler> clazz) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return new HandlerHolder(size, perHandlerPoolSize, clazz);
    }

    public EventHandler nextHandler() {
        return list.get(IDX.getAndIncrement() % size);
    }


    public void startHandle(EventHandler eventHandler) {
        if (RUNNER.add(eventHandler)) {
            Thread thread = new Thread(eventHandler);
            thread.setDaemon(true);
            thread.setName(Thread.currentThread().getName() + "-EventHandler" + IDX.get());
            thread.start();
            log.debug("create handler");
        }
    }
}
