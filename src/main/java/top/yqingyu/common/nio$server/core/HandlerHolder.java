package top.yqingyu.common.nio$server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.utils.ThreadUtil;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.HashSet;
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
    private final int perHandlerPoolSize;

    private  long workerKeepaliveTime;
    private final AtomicInteger IDX = new AtomicInteger();

    public final HashSet<EventHandler> RUNNER = new HashSet<>();


    private HandlerHolder(int size, int perHandlerPoolSize, Class<? extends EventHandler> clazz) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.size = size;
        this.perHandlerPoolSize = perHandlerPoolSize;
        this.list = new ArrayList<>(size);
        Constructor<? extends EventHandler> constructor = clazz.getConstructor(Selector.class);
        for (int i = 0; i < size; i++) {
            list.add(constructor.newInstance(Selector.open()));
        }
    }

    public static HandlerHolder createDefaultSize(Class<? extends EventHandler> clazz) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        int i = Runtime.getRuntime().availableProcessors();
        return createFixed(i * 2, 2, clazz);
    }

    public static HandlerHolder createDefaultSize(Class<? extends EventHandler> clazz, int perHandlerPoolSize) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        int i = Runtime.getRuntime().availableProcessors();
        return new HandlerHolder(i * 2, perHandlerPoolSize, clazz);
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
            String name = Thread.currentThread().getName();
            thread.setDaemon(true);
            thread.setName(name + "-Handler" + IDX.get());
            eventHandler.POOL = ThreadUtil.createQyFixedThreadPool(perHandlerPoolSize, new ThreadUtil.QyCurrentPoolNameFactory().QyThreadFactory(thread.getName(), "th"));
            thread.start();
            log.debug("create handler");
        }
    }
}
