package top.yqingyu.common.server$aio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.exception.IllegalStartupOrderException;
import top.yqingyu.common.qydata.ConcurrentQyMap;
import top.yqingyu.common.utils.LocalDateTimeUtil;
import top.yqingyu.common.utils.ThreadUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadFactory;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.server$aio.CreateServer
 * @description
 * @createTime 2023年02月09日 20:21:00
 */
public class CreateServer {

    private static final Logger logger = LoggerFactory.getLogger(CreateServer.class);
    private AsynchronousServerSocketChannel serverSocketChannel;
    private int threads;
    private Class<? extends Session> eventHandler;
    private String name = "aio";
    private String perThName = "th";
    private final int port = 4728;

    public final LocalDateTime startTime;
    private SessionBridge sessionBridge;

    private boolean notInit = true;
    private boolean notBind = true;
    private boolean notHandler = true;

    public final static ConcurrentQyMap qyMap = new ConcurrentQyMap();

    CreateServer() {
        startTime = LocalDateTime.now();
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
    public CreateServer init() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (notHandler)
            throw new IllegalStartupOrderException("setHandler(EventHandler handler)");
        notInit = false;
        SessionBridge.sessionClazz = this.eventHandler;
        ThreadFactory threadFactory = ThreadUtil.createThFactoryG(name, perThName);
        AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withFixedThreadPool(threads, threadFactory);
        this.serverSocketChannel = AsynchronousServerSocketChannel
                .open(channelGroup);
        sessionBridge = new SessionBridge(this.serverSocketChannel);
        return this;
    }

    public CreateServer bind(int port) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (notInit) {
            init();
        }
        notBind = false;
        this.serverSocketChannel.bind(new InetSocketAddress(port));
        logger.info("server {} bind port {}", name, port);
        return this;
    }

    public <T> CreateServer setHandler(Class<? extends Session> handler) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        notHandler = false;
        this.eventHandler = handler;
        return this;
    }

    public void start() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (notBind) bind(this.port);
        this.serverSocketChannel.accept(sessionBridge, new AcceptHandler(sessionBridge));
        logger.info("{} start success cost: {} micros", name, LocalDateTimeUtil.between(startTime, LocalDateTime.now(), ChronoUnit.MICROS));
    }

}
