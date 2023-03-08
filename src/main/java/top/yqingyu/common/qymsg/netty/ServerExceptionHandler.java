package top.yqingyu.common.qymsg.netty;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.exception.IllegalQyMsgException;

import java.net.SocketException;

public interface ServerExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ServerExceptionHandler.class);

    default void handle(ChannelHandlerContext ctx, Throwable cause) {
        String causeMessage = cause.getMessage();
        if (cause instanceof IllegalQyMsgException) {
            logger.warn("已知异常 {} ", causeMessage, cause);
        } else if (cause instanceof SocketException && "Connection reset".equals(causeMessage)) {
            logger.debug("链接重置 {}", ctx.hashCode());
        } else {
            logger.warn("未知异常 {} ", causeMessage, cause);
        }
    }
}
