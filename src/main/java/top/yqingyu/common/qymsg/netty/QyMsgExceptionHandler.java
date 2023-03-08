package top.yqingyu.common.qymsg.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

public class QyMsgExceptionHandler extends ChannelDuplexHandler {
    private final ServerExceptionHandler serverExceptionHandler;

    public QyMsgExceptionHandler() {
        this.serverExceptionHandler = new ServerExceptionHandler() {};
    }

    public QyMsgExceptionHandler(ServerExceptionHandler serverExceptionHandler) {
        this.serverExceptionHandler = serverExceptionHandler;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        serverExceptionHandler.handle(ctx, cause);
    }
}
