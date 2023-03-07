package top.yqingyu.common.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class QyCodec extends ByteToMessageDecoder {
    Logger logger = LoggerFactory.getLogger(QyCodec.class);

    public QyCodec(Logger logger) {
        super();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {
        int i = in.readableBytes();
        byte[] bytes = new byte[i];
        in.readBytes(bytes);
        String s = new String(bytes, StandardCharsets.UTF_8);
        logger.info("\n{}", new String(bytes, StandardCharsets.UTF_8));
        logger.info("decode");
    }
}