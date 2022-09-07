package top.yqingyu.common.nio$server.core;

import lombok.extern.slf4j.Slf4j;
import top.yqingyu.common.nio$server.event.EventHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.core.HandlerRouter
 * @description
 * @createTime 2022年09月07日 14:14:00
 */
@Slf4j
public class HandlerRouter {

    private final ServerSocketChannel serverSocketChannel;
    private final HandlerHolder handlerHolder;


    public HandlerRouter(ServerSocketChannel serverSocketChannel, HandlerHolder handler) {
        this.serverSocketChannel = serverSocketChannel;
        this.handlerHolder = handler;
    }


    public static HandlerRouter createDefault(ServerSocketChannel serverSocketChannel, Class<? extends EventHandler> clazz) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return new HandlerRouter(serverSocketChannel, HandlerHolder.createDefaultSize(clazz));
    }

    public static HandlerRouter createDefault(ServerSocketChannel serverSocketChannel, int size, int perHandlerPoolSize, Class<? extends EventHandler> clazz) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return new HandlerRouter(serverSocketChannel, HandlerHolder.createFixed(size, perHandlerPoolSize, clazz));
    }

    /**
     * @author YYJ
     * @description 将通道注册到selector池中
     */
    public void route() {

        try {
            EventHandler eventHandler = handlerHolder.nextHandler();
            Selector nextSelector = eventHandler.getSelector();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(nextSelector, SelectionKey.OP_READ);
            //启动处理器
            handlerHolder.startHandle(eventHandler);
            nextSelector.wakeup();
        } catch (Exception e) {
            log.error("分发器异常", e);
        }

    }
}
