package top.yqingyu.common.qymsg.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import top.yqingyu.common.qymsg.QyMsg;

@ChannelHandler.Sharable
public class DemoMsgHandler extends QyMsgServerHandler {
    @Override
    protected QyMsg handle(ChannelHandlerContext ctx, QyMsg msg) {
        //无条件回显。
        return msg;
    }
}
