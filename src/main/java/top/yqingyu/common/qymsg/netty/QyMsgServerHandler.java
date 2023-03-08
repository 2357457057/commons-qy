package top.yqingyu.common.qymsg.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.yqingyu.common.qymsg.QyMsg;

public abstract class QyMsgServerHandler extends SimpleChannelInboundHandler<QyMsg> {
    @Override
    protected final void channelRead0(ChannelHandlerContext ctx, QyMsg msg) throws Exception {
        QyMsg handle = handle(ctx, msg);
        Channel channel = ctx.channel();
        channel.write(handle);
        channel.flush();
    }

    protected abstract QyMsg handle(ChannelHandlerContext ctx, QyMsg msg) throws Exception;
}
