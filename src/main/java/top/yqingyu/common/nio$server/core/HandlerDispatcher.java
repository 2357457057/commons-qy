package top.yqingyu.common.nio$server.core;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.core.HandlerDispatcher
 * @description
 * @createTime 2022年09月07日 18:14:00
 */
@Slf4j
public class HandlerDispatcher implements Runnable {
    private final Selector selector;

    private Thread thread;

    public HandlerDispatcher(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    iterator.remove();
                    //调度路由器
                    route(next);
                }
            }
        } catch (IOException e) {
            log.error("分配器异常", e);
        }
    }


    private void route(SelectionKey next) {
        HandlerRouter attachment = (HandlerRouter) next.attachment();
        if (attachment != null) {
            attachment.route();
        }
    }

    public HandlerDispatcher start(String name) {
        thread = new Thread(this);
        thread.setName(name);
        thread.start();
        return this;
    }

    public void stop() {
        this.thread.interrupt();
    }
}