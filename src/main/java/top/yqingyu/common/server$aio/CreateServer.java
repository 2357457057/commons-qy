package top.yqingyu.common.server$aio;

import top.yqingyu.common.exception.IllegalStartupOrderException;
import top.yqingyu.common.qydata.ConcurrentQyMap;
import top.yqingyu.common.server$aio.core.EventHandler;
import top.yqingyu.common.utils.ThreadUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ThreadFactory;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.server$aio.CreateServer
 * @description
 * @createTime 2023年02月09日 20:21:00
 */
public class CreateServer {

    private AsynchronousServerSocketChannel serverSocketChannel;
    private int threads;
    private EventHandler eventHandler;
    private String name = "http-aio";
    private String perThName = "th";
    private int port = 4728;

    private boolean notInit = true;
    private boolean notBind = true;
    private boolean notHandler = true;

    public final static ConcurrentQyMap  qyMap = new ConcurrentQyMap();

    CreateServer() {
        threads = Runtime.getRuntime().availableProcessors() * 2;
    }

    public static CreateServer create() {
        return new CreateServer();
    }

    public CreateServer setServerName(String name) {
        this.name = name;
        return this;
    }

    public CreateServer setPerThName(String perThName) {
        this.perThName = perThName;
        return this;
    }

    /**
     * default (cpu core num)*2
     *
     * @author YYJ
     * @version 1.0.0
     * @description
     */
    public CreateServer setThreadNo(int num) {
        this.threads = num;
        return this;
    }


    /*====================================================================================================================*/
    /*===================================================主要方法==========================================================*/
    /*====================================================================================================================*/
    public CreateServer init() throws IOException {
        notInit = false;
        ThreadFactory threadFactory = ThreadUtil.createThFactoryG(name, perThName);
        AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withFixedThreadPool(threads, threadFactory);
        this.serverSocketChannel = AsynchronousServerSocketChannel
                .open(channelGroup);
        return this;
    }

    public CreateServer bind(int port) throws IOException {
        if (notInit) {
            init();
        }
        notBind = false;
        this.serverSocketChannel.bind(new InetSocketAddress(port));
        return this;
    }

    public CreateServer setHandler(EventHandler handler) {
        notHandler = false;
        this.eventHandler = handler;
        return this;
    }

    public void start() throws IOException {
        if (notInit) init();
        if (notBind) bind(this.port);
        if (notHandler)
            throw new IllegalStartupOrderException("setHandler(EventHandler handler)");
        this.serverSocketChannel.accept(this, eventHandler);
    }

    public AsynchronousServerSocketChannel getSocketChannel() {
        return serverSocketChannel;
    }
}
