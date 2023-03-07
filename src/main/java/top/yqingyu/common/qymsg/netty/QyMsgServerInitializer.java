package top.yqingyu.common.qymsg.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import top.yqingyu.common.qymsg.MsgTransfer;
import top.yqingyu.common.utils.ThreadUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;


public class QyMsgServerInitializer extends ChannelInitializer<SocketChannel> {

    private final QyMsgServerHandler qyMsgServerHandler;

    public QyMsgServerInitializer(QyMsgServerHandler qyMsgServerHandler) {
        MsgTransfer.init(32,99999, ThreadUtil.createQyFixedThreadPool(10,"",""));
        this.qyMsgServerHandler = qyMsgServerHandler;
    }


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new BytesDecodeQyMsg());
        pipeline.addLast(qyMsgServerHandler);
        pipeline.addLast(new QyMsgEncodeBytes());
    }
}
