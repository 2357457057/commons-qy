package top.yqingyu.common.qymsg.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import top.yqingyu.common.qymsg.AssemblyMsg;
import top.yqingyu.common.qymsg.QyMsg;

import java.util.ArrayList;

public class QyMsgEncodeBytes extends MessageToByteEncoder<QyMsg> {
    @Override
    protected void encode(ChannelHandlerContext ctx, QyMsg msg, ByteBuf out) throws Exception {
        ArrayList<byte[]> MsgBytes = AssemblyMsg.assembly(msg);
        for (byte[] bytes : MsgBytes) {
            out.writeBytes(bytes);
        }

    }
}
