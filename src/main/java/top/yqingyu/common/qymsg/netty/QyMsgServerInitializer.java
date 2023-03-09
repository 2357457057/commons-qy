package top.yqingyu.common.qymsg.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import top.yqingyu.common.qymsg.MsgTransfer;

/**
 * 初始化工具
 */
public class QyMsgServerInitializer extends ChannelInitializer<SocketChannel> {

    private final QyMsgServerHandler qyMsgServerHandler;
    private ServerExceptionHandler serverExceptionHandler;

    public QyMsgServerInitializer(QyMsgServerHandler qyMsgServerHandler) {
        //TEST CODE   MsgTransfer.init(32, 99999);
        this.qyMsgServerHandler = qyMsgServerHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new BytesDecodeQyMsg());
        pipeline.addLast(qyMsgServerHandler);
        pipeline.addLast(new QyMsgEncodeBytes());
        pipeline.addLast(serverExceptionHandler == null ? new QyMsgExceptionHandler() : new QyMsgExceptionHandler(serverExceptionHandler));

    }

    public void setQyMsgExceptionHandler(ServerExceptionHandler qyMsgExceptionHandler) {
        this.serverExceptionHandler = qyMsgExceptionHandler;
    }
}
