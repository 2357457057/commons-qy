package top.yqingyu.common.qymsg.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.yqingyu.common.qymsg.QyMsg;

public abstract class QyMsgServerHandler extends SimpleChannelInboundHandler<QyMsg> {
    @Override
    protected final void channelRead0(ChannelHandlerContext ctx, QyMsg msg) throws Exception {
        Channel channel = ctx.channel();
        channel.write(msg);
        channel.flush();
    }

    protected abstract void handle(ChannelHandlerContext ctx, QyMsg msg);
}
