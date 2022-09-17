package top.yqingyu.common.nio$server;

import cn.hutool.core.date.LocalDateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.nio$server.core.HandlerDispatcher;
import top.yqingyu.common.nio$server.core.HandlerRouter;
import top.yqingyu.common.nio$server.core.EventHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.core.CreateServer
 * @description
 * @createTime 2022年09月07日 19:49:00
 */

public class CreateServer {
    private static final Logger log = LoggerFactory.getLogger(CreateServer.class);
    private Integer port;
    private String serverName;
    private final Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private Class<? extends EventHandler> eventClazz;

    private final LocalDateTime startTime;

    private HandlerDispatcher handlerDispatcher;

    private CreateServer(Selector selector) {
        this.startTime = getTime();
        this.selector = selector;
    }

    public CreateServer(int port, String name, Selector selector, ServerSocketChannel serverSocketChannel, Class<? extends EventHandler> eventClazz, HandlerDispatcher handlerDispatcher) {
        this.startTime = getTime();
        this.port = port;
        this.serverName = name;
        this.selector = selector;
        this.serverSocketChannel = serverSocketChannel;
        this.eventClazz = eventClazz;
        this.handlerDispatcher = handlerDispatcher;
    }

    /**
     * step1
     *
     * @param port       服务器端口
     * @param serverName 服务器名称
     * @author YYJ
     * @description
     */
    public static CreateServer createDefault(int port, String serverName) throws IOException {
        CreateServer server = new CreateServer(Selector.open());
        server.serverSocketChannel = ServerSocketChannel.open();
        server.serverSocketChannel.configureBlocking(false);
        server.port = port;
        server.serverName = serverName;
        return server;
    }


    /**
     * step1
     *
     * @param serverName 服务器名称
     * @author YYJ
     * @description
     */
    public static CreateServer createDefault(String serverName) throws IOException {
        CreateServer server = new CreateServer(Selector.open());
        server.serverSocketChannel = ServerSocketChannel.open();
        server.serverSocketChannel.configureBlocking(false);
        server.serverName = serverName;
        return server;
    }

    /**
     * @param handlerRouter 绑定路由
     * @author YYJ
     * @description
     */
    public CreateServer setRouter(HandlerRouter handlerRouter) throws ClosedChannelException {
        if (this.eventClazz == null)
            throw new RuntimeException("where is EventHandler ?");
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, handlerRouter);
        return this;
    }

    /**
     * step 3
     *
     * @author YYJ
     * @description 创建默认路由，即处理器线程数量*2的handler,并为每个handler创建一个 2size的 线程池
     */
    public CreateServer defaultRouter() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return this.setRouter(HandlerRouter.createDefault(serverSocketChannel, eventClazz));
    }

    /**
     * @author YYJ
     * @description 创建默认路由，指定handler数量,并为每个handler创建一个 2 size的 线程池
     */
    public CreateServer defaultFixRouter(int size) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return this.setRouter(HandlerRouter.createDefault(serverSocketChannel, size, 2, eventClazz));
    }


    /**
     * @author YYJ
     * @description 创建默认路由，默认handler数量,并为每个handler指定 size 的任务线程池
     */
    public CreateServer defaultRouterPerPool(int perHandlerPoolSize) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return this.setRouter(HandlerRouter.createDefault(serverSocketChannel, eventClazz, perHandlerPoolSize));
    }

    /**
     * 创建默认路由，指定handler数量,并为每个handler创建一个 指定 size的 线程池
     *
     * @param size               处理器数量
     * @param perHandlerPoolSize 每个处理器的数据线程池数
     * @author YYJ
     */
    public CreateServer defaultFixRouter(int size, int perHandlerPoolSize) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return this.setRouter(HandlerRouter.createDefault(serverSocketChannel, size, perHandlerPoolSize, eventClazz));
    }

    /**
     * step 2
     * 实现一个继承{@link EventHandler} 的事件处理类
     *
     * @param eventClazz 事件处理类
     * @author YYJ
     */
    public CreateServer implEvent(Class<? extends EventHandler> eventClazz) {
        this.eventClazz = eventClazz;
        log.debug("Configured Event Handlers");
        return this;
    }


    /**
     * 启动服务器
     * step 5
     *
     * @author YYJ
     * @description
     */
    public CreateServer start() {
        if (this.handlerDispatcher == null) {
            handlerDispatcher = new HandlerDispatcher(selector);
        }
        handlerDispatcher.start(serverName == null ? "QyServer" : serverName);
        long time = LocalDateTimeUtil.between(this.startTime, getTime(), ChronoUnit.MICROS);
        log.info("{} start success ! cost: {} micros", serverName, time);
        return this;
    }


    /**
     * 销毁服务器
     *
     * @author YYJ
     * @description
     */
    public void stop() throws IOException {
        handlerDispatcher.stop();
        serverSocketChannel.close();
        log.info("{} stop success !  port{} unbind", serverName, port);
    }

    /**
     * step 4
     * 监听端口 若未在创建构造方法初始化将监听本端口
     *
     * @author YYJ
     * @description
     */
    public CreateServer listenPort(int port) throws IOException {
        if (this.port == null) {
            this.port = port;
        } else
            log.warn("use constructor init port {}", this.port);

        ServerSocket serverSocket = serverSocketChannel.socket();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(this.port);
        serverSocket.bind(inetSocketAddress);
        log.info("server {} bind port {}", serverName, this.port);
        return this;
    }

    /**
     * step 4
     * 监听端口 若未在创建构造方法初始化将监听本端口
     *
     * @author YYJ
     * @description
     */
    public CreateServer listenPort() throws IOException {
        if (this.port == null) {
            throw new RuntimeException("port not init");
        }
        ServerSocket serverSocket = serverSocketChannel.socket();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(this.port);
        serverSocket.bind(inetSocketAddress);
        log.info("server {} bind port {}", serverName, this.port);
        return this;
    }

    @SuppressWarnings("all")
    public CreateServer loadingEventResource() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        LocalDateTime time = getTime();
        Method method = eventClazz.getDeclaredMethod("loading");
        method.setAccessible(true);
        EventHandler eventHandler = eventClazz.getConstructor().newInstance();
        method.invoke(eventHandler);

        log.info("{} loading event resource cost: {} micros",serverName, LocalDateTimeUtil.between(time, getTime(), ChronoUnit.MICROS));
        return this;
    }


    private LocalDateTime getTime() {
        return LocalDateTime.now();
    }
}
