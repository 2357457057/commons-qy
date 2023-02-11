package top.yqingyu.common.server$aio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.bean.NetChannel;
import top.yqingyu.common.qydata.ConcurrentQyMap;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.server$aio.SessionBridge
 * @description
 * @createTime 2023年02月11日 15:08:00
 */
public class SessionBridge {
    public static final ConcurrentQyMap<Integer, ConcurrentQyMap<String, Object>> NET_CHANNELS = new ConcurrentQyMap<>();
    private final AsynchronousServerSocketChannel serverSocketChannel;
    static Logger logger = LoggerFactory.getLogger(ReadHandler.class);
    static Class<? extends Session> sessionClazz;
    final Session session;
    private NetChannel netChannel;
    Integer readResult;
    Integer writeResult;

    SessionBridge(AsynchronousServerSocketChannel serverSocketChannel) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.serverSocketChannel = serverSocketChannel;
        this.session = sessionClazz.getDeclaredConstructor().newInstance();
        this.session.setSessionBridge(this);
    }

    void ready(AsynchronousSocketChannel channel) {
        logger.warn("ready");
        try {
            SessionBridge sessionBridge = new SessionBridge(this.serverSocketChannel);
            serverSocketChannel.accept(sessionBridge, new AcceptHandler(sessionBridge));
            this.netChannel = new NetChannel(channel);
            NET_CHANNELS.put(netChannel.hashCode(),
                    new ConcurrentQyMap<String, Object>()
                            .putConsecutive("NetChannel", netChannel)
                            .putConsecutive("LocalDateTime", LocalDateTime.now())
            );
            session.ready();
        } catch (Throwable exec) {
            this.session.doLogicError(exec);
        }
    }


    final CountDownLatch readLock = new CountDownLatch(1);
    final CountDownLatch writeLock = new CountDownLatch(1);

    Integer writeComplete() throws InterruptedException {
        writeLock.await();
        return writeResult;
    }

    Integer readComplete() throws InterruptedException {
        readLock.await();
        return readResult;
    }

    public void read(ByteBuffer buffer, long timeout, TimeUnit unit) {
        netChannel.read(buffer, timeout, unit, this, new ReadHandler(this));
    }

    public void write(ByteBuffer buffer, long timeout, TimeUnit unit) {
        netChannel.write(buffer, timeout, unit, this, new WriteHandler(this));
    }


    void readError(Throwable exec) {
        readResult = -1;
        session.doLogicError(exec);
    }

    void writeError(Throwable exec) {
        writeResult = -1;
        session.doLogicError(exec);
    }

    public NetChannel getNetChannel() {
        return netChannel;
    }
}
