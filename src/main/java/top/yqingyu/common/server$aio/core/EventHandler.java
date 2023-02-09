package top.yqingyu.common.server$aio.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.bean.NetChannel;
import top.yqingyu.common.qydata.ConcurrentQyMap;
import top.yqingyu.common.server$aio.CreateServer;
import top.yqingyu.common.utils.IoUtil;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.time.LocalDateTime;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.server$aio.core.EventHandler
 * @description
 * @createTime 2023年02月09日 21:11:00
 */
public class EventHandler implements CompletionHandler<AsynchronousSocketChannel, CreateServer> {
    static final Logger logger = LoggerFactory.getLogger(EventHandler.class);

    protected final ConcurrentQyMap<Integer, ConcurrentQyMap<String, Object>> NET_CHANNELS = new ConcurrentQyMap<>();

    @Override
    public final void completed(AsynchronousSocketChannel channel, CreateServer attachment) {
        attachment.getSocketChannel().accept(attachment, this);
        NetChannel netChannel = new NetChannel(channel);
        NET_CHANNELS.put(netChannel.hashCode(),
                new ConcurrentQyMap<String, Object>()
                        .putConsecutive("NetChannel", netChannel)
                        .putConsecutive("LocalDateTime", LocalDateTime.now())
        );
        IO(netChannel, attachment);
    }

    @Override
    public final void failed(Throwable exc, CreateServer attachment) {
        exception(exc, attachment);
    }


    public void IO(NetChannel channel, CreateServer attachment) {
        AsynchronousSocketChannel aChannel = channel.getAChannel();
        try {
            byte[] bytes = IoUtil.readBytes(aChannel, 1024);
            logger.info(new String(bytes));
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public void exception(Throwable exc, CreateServer attachment) {
        logger.error("{}", attachment, exc);
    }


}
