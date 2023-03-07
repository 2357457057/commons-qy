package qymsg.testMsg;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import top.yqingyu.common.qymsg.QyMsg;
import top.yqingyu.common.qymsg.netty.QyMsgServerHandler;
@ChannelHandler.Sharable
public class MsgHandler extends QyMsgServerHandler {
    @Override
    protected void handle(ChannelHandlerContext ctx, QyMsg msg) {
        System.out.println(msg.toString());
    }
}
