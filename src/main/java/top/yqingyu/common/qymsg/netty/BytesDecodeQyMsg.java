package top.yqingyu.common.qymsg.netty;

import com.alibaba.fastjson2.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.exception.IllegalQyMsgException;
import top.yqingyu.common.qymsg.DataType;
import top.yqingyu.common.qymsg.MsgTransfer;
import top.yqingyu.common.qymsg.MsgType;
import top.yqingyu.common.qymsg.QyMsg;
import top.yqingyu.common.utils.IoUtil;
import top.yqingyu.common.utils.StringUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static top.yqingyu.common.qymsg.Dict.*;

public class BytesDecodeQyMsg extends ByteToMessageDecoder {

    final static BlockingQueue<QyMsg> segmentation$queue = new LinkedBlockingQueue<>();

    private static final Logger log = LoggerFactory.getLogger(BytesDecodeQyMsg.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        byte[] readBytes = readBytes(in, HEADER_LENGTH);
        String s = new String(readBytes, StandardCharsets.UTF_8);

        if (StringUtil.isNotBlank(s) && s.length() != HEADER_LENGTH) {
            throw new IllegalQyMsgException("消息头长度非法 消息头为：" + s);
        }
        char $0 = s.charAt(MSG_TYPE_IDX);//msg  type
        char $1 = s.charAt(DATA_TYPE_IDX);//data type
        char $2 = s.charAt(SEGMENTATION_IDX);//是否分片
        String $3 = s.substring(MSG_LENGTH_IDX_START, MSG_LENGTH_IDX_END);     //后五位

        boolean segmentation;
        try {
            segmentation = MsgTransfer.SEGMENTATION_2_BOOLEAN($2);
        } catch (Exception e) {
            System.out.println(new String(readBytes(in, in.readableBytes()), StandardCharsets.UTF_8));
            ctx.fireChannelRead(null);
            throw new IllegalQyMsgException("非法分片字符: " + $2, e);
        }

        if (segmentation) {
            MsgType msgType;
            try {
                msgType = MsgTransfer.CHAR_2_MSG_TYPE($0);
            } catch (Exception e) {
                throw new IllegalQyMsgException("非法的消息类型: " + s, e);
            }
            DataType dataType;
            try {
                dataType = MsgTransfer.CHAR_2_DATA_TYPE($1);
            } catch (Exception e) {
                throw new IllegalQyMsgException("非法的数据类型: " + s, e);
            }

            QyMsg parse = new QyMsg(msgType, dataType);
            parse.setSegmentation(true);
            readBytes = readBytes(in, SEGMENTATION_INFO_LENGTH);
            String segmentationInfo = new String(readBytes, StandardCharsets.UTF_8);
            parse.setPartition_id(segmentationInfo.substring(PARTITION_ID_IDX_START, PARTITION_ID_IDX_END));
            parse.setNumerator(Integer.parseInt(segmentationInfo.substring(NUMERATOR_IDX_START, NUMERATOR_IDX_END), MsgTransfer.MSG_LENGTH_RADIX));
            parse.setDenominator(Integer.parseInt(segmentationInfo.substring(DENOMINATOR_IDX_START, DENOMINATOR_IDX_END), MsgTransfer.MSG_LENGTH_RADIX));
            readBytes = readBytes(in, Integer.parseInt($3, MsgTransfer.MSG_LENGTH_RADIX));
            parse.putMsg(readBytes);
            segmentation$queue.add(parse);
            //待处理
            log.debug("part msg id: {} the part {} of {}", parse.getPartition_id(), parse.getNumerator(), parse.getDenominator());
        } else {
            MsgType msgType;
            try {
                msgType = MsgTransfer.CHAR_2_MSG_TYPE($0);
            } catch (Exception e) {
                throw new IllegalQyMsgException("非法的消息类型: " + s, e);
            }
            QyMsg qyMsg;
            switch (msgType) {
                case AC -> {
                    qyMsg = AC_Disassembly($3, in);
                }
                case HEART_BEAT -> {
                    qyMsg = HEART_BEAT_Disassembly($0, $1, $2, $3, in);
                }
                case ERR_MSG -> {
                    qyMsg = ERR_MSG_Disassembly($0, $1, $2, $3, in);
                }
                default -> {
                    qyMsg = NORM_MSG_Disassembly($0, $1, $3, in);
                }
            }
            ctx.fireChannelRead(qyMsg);
        }
    }

    static byte[] readBytes(ByteBuf buf, int length) {
        byte[] readBytes = new byte[length];
        buf.readBytes(readBytes);
        return readBytes;
    }


    /**
     * @param msg_length 消息长度
     * @param in         流
     * @description 认证消息解析
     */
    private static QyMsg AC_Disassembly(String msg_length, ByteBuf in) throws IOException {
        byte[] bytes = readBytes(in, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX));
        return JSON.parseObject(bytes, QyMsg.class);
    }

    /**
     * 心跳消息组装
     */
    private static QyMsg HEART_BEAT_Disassembly(char msg_type, char data_type, char segmentationC, String msg_length, ByteBuf in) throws IOException {
        MsgType msgType;
        try {
            msgType = MsgTransfer.CHAR_2_MSG_TYPE(msg_type);
        } catch (Exception e) {
            throw new IllegalQyMsgException("非法的消息类型: " + msg_type, e);
        }
        DataType dataType;
        try {
            dataType = MsgTransfer.CHAR_2_DATA_TYPE(data_type);
        } catch (Exception e) {
            throw new IllegalQyMsgException("非法的数据类型: " + data_type, e);
        }
        boolean segmentation;
        try {
            segmentation = MsgTransfer.SEGMENTATION_2_BOOLEAN(segmentationC);
        } catch (Exception e) {
            throw new IllegalQyMsgException("非法分片字符: " + segmentationC, e);
        }

        QyMsg qyMsg = new QyMsg(msgType, dataType);
        qyMsg.setSegmentation(segmentation);
        byte[] bytes = readBytes(in, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX));
        qyMsg.setFrom(new String(bytes, StandardCharsets.UTF_8));
        return qyMsg;
    }

    /**
     * 常规消息组装
     */
    private static QyMsg NORM_MSG_Disassembly(char msg_type, char data_type, String msg_length, ByteBuf in) throws IOException, ClassNotFoundException {
        MsgType msgType;
        try {
            msgType = MsgTransfer.CHAR_2_MSG_TYPE(msg_type);
        } catch (Exception e) {
            throw new IllegalQyMsgException("非法的消息类型: " + msg_type, e);
        }
        DataType dataType;
        try {
            dataType = MsgTransfer.CHAR_2_DATA_TYPE(data_type);
        } catch (Exception e) {
            throw new IllegalQyMsgException("非法的数据类型: " + data_type, e);
        }
        switch (dataType) {
            case STRING -> {
                byte[] bytes = readBytes(in, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX));
                String s = new String(bytes, StandardCharsets.UTF_8);
                String from = s.substring(0, CLIENT_ID_LENGTH);
                String msg = s.substring(CLIENT_ID_LENGTH);
                QyMsg qyMsg = new QyMsg(msgType, dataType);
                qyMsg.setFrom(from);
                qyMsg.putMsg(msg);
                return qyMsg;
            }
            case OBJECT -> {
                byte[] bytes = readBytes(in, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX));
                return IoUtil.deserializationObj(bytes, QyMsg.class);
            }
            case STREAM -> {
                return streamDeal(msg_type, data_type, msg_length, in);
            }
            default -> { //JSON FILE
                byte[] bytes = readBytes(in, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX));
                return JSON.parseObject(bytes, QyMsg.class);
            }
        }
    }

    /**
     * 异常消息组装
     */
    private static QyMsg ERR_MSG_Disassembly(char msg_type, char data_type, char segmentation, String msg_length, ByteBuf in) throws IOException {
        DataType dataType;
        try {
            dataType = MsgTransfer.CHAR_2_DATA_TYPE(data_type);
        } catch (Exception e) {
            throw new IllegalQyMsgException("非法的数据类型:" + data_type, e);
        }
        if (DataType.JSON.equals(dataType)) {
            byte[] bytes = readBytes(in, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX));
            return JSON.parseObject(bytes, QyMsg.class);
        } else {
            return streamDeal(msg_type, data_type, msg_length, in);
        }
    }

    /**
     * @author YYJ
     * @description 流类型数据处理
     */
    private static QyMsg streamDeal(char msg_type, char data_type, String msg_length, ByteBuf in) throws IOException {
        MsgType msgType;
        try {
            msgType = MsgTransfer.CHAR_2_MSG_TYPE(msg_type);
        } catch (Exception e) {
            throw new IllegalQyMsgException("非法的消息类型: " + msg_type, e);
        }
        DataType dataType;
        try {
            dataType = MsgTransfer.CHAR_2_DATA_TYPE(data_type);
        } catch (Exception e) {
            throw new IllegalQyMsgException("非法的数据类型: " + data_type, e);
        }
        QyMsg qyMsg = new QyMsg(msgType, dataType);
        byte[] bytes = readBytes(in, CLIENT_ID_LENGTH);
        qyMsg.setFrom(new String(bytes, StandardCharsets.UTF_8));
        qyMsg.putMsg(readBytes(in, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX) - CLIENT_ID_LENGTH));
        return qyMsg;
    }
}
