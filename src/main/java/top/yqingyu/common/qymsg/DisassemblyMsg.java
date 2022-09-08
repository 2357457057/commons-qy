package top.yqingyu.common.qymsg;

import com.alibaba.fastjson2.JSON;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.utils.IoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.qymsg.DisassemblyMsg
 * @description
 * @createTime 2022年09月06日 10:36:00
 */

public class DisassemblyMsg {

    private static final Logger log = LoggerFactory.getLogger(DisassemblyMsg.class);
    /**
     * @param sleep 服务端出口有限，需增加间隔时间，否则会导致流读不完整
     * @author YYJ
     * @description 消息解析
     */
    public static QyMsg disassembly(Socket socket, BlockingQueue<QyMsg> segmentation$queue, long sleep) throws IOException, ClassNotFoundException, InterruptedException {
        InputStream inputStream = socket.getInputStream();
        byte[] readBytes = IoUtil.readBytes(inputStream, 8);
        Thread.sleep(sleep);
        String s = new String(readBytes, StandardCharsets.UTF_8);
        char $0 = s.charAt(0);//msg  type
        char $1 = s.charAt(1);//data type
        char $2 = s.charAt(2);//是否分片
        String $3 = s.substring(4);     //后五位


        if (MsgTransfer.SEGMENTATION_2_BOOLEAN($2)) {
            QyMsg parse = new QyMsg(MsgTransfer.CHAR_2_MSG_TYPE($0), MsgTransfer.CHAR_2_DATA_TYPE($1));
            parse.setSegmentation(MsgTransfer.SEGMENTATION_2_BOOLEAN($2));
            readBytes = IoUtil.readBytes(inputStream, 18);
            String partition_numerator_denominator = new String(readBytes, StandardCharsets.UTF_8);
            parse.setPartition_id(partition_numerator_denominator.substring(0, 16));
            parse.setNumerator(Integer.parseInt(partition_numerator_denominator.substring(16, 17), MsgTransfer.MSG_LENGTH_RADIX));
            parse.setDenominator(Integer.parseInt(partition_numerator_denominator.substring(17, 18), MsgTransfer.MSG_LENGTH_RADIX));
            readBytes = IoUtil.readBytes(inputStream, Integer.parseInt($3, MsgTransfer.MSG_LENGTH_RADIX));
            parse.putMsg(readBytes);
            segmentation$queue.add(parse);
            log.debug("part msg id: {} the part {} of {}", parse.getPartition_id(), parse.getNumerator(), parse.getDenominator());
            return null;
        } else {
            MsgType msgType = MsgTransfer.CHAR_2_MSG_TYPE($0);
            if (MsgType.AC.equals(msgType)) {

                //认证消息解析
                return AC_Disassembly($3, inputStream);
            } else if (MsgType.HEART_BEAT.equals(msgType)) {

                //心跳消息解析
                return HEART_BEAT_Disassembly($0, $1, $2, $3, inputStream);
            } else if (MsgType.NORM_MSG.equals(msgType)) {

                //普通消息解析
                return NORM_MSG_Disassembly($0, $1, $2, $3, inputStream);
            } else if (MsgType.ERR_MSG.equals(msgType)) {
                //异常消息解析
                return ERR_MSG_Disassembly($0, $1, $2, $3, inputStream);
            } else {
                //普通消息解析
                return NORM_MSG_Disassembly($0, $1, $2, $3, inputStream);
            }
        }
    }

    /**
     * @author YYJ
     * @description 消息解析
     */
    public static QyMsg disassembly(SocketChannel socketChannel, BlockingQueue<QyMsg> segmentation$queue, long sleep) throws IOException, ClassNotFoundException, InterruptedException {

        byte[] readBytes = IoUtil.readBytes(socketChannel, 8);
        String s = new String(readBytes, StandardCharsets.UTF_8);
        Thread.sleep(sleep);
        char $0 = s.charAt(0);//msg  type
        char $1 = s.charAt(1);//data type
        char $2 = s.charAt(2);//是否分片
        String $3 = s.substring(4);     //后五位


        if (MsgTransfer.SEGMENTATION_2_BOOLEAN($2)) {
            QyMsg parse = new QyMsg(MsgTransfer.CHAR_2_MSG_TYPE($0), MsgTransfer.CHAR_2_DATA_TYPE($1));
            parse.setSegmentation(MsgTransfer.SEGMENTATION_2_BOOLEAN($2));
            readBytes = IoUtil.readBytes(socketChannel, 18);
            String partition_numerator_denominator = new String(readBytes, StandardCharsets.UTF_8);
            parse.setPartition_id(partition_numerator_denominator.substring(0, 16));
            parse.setNumerator(Integer.parseInt(partition_numerator_denominator.substring(16, 17), MsgTransfer.MSG_LENGTH_RADIX));
            parse.setDenominator(Integer.parseInt(partition_numerator_denominator.substring(17, 18), MsgTransfer.MSG_LENGTH_RADIX));
            readBytes = IoUtil.readBytes(socketChannel, Integer.parseInt($3, MsgTransfer.MSG_LENGTH_RADIX));
            parse.putMsg(readBytes);
            segmentation$queue.add(parse);
            log.debug("part msg id: {} the part {} of {}", parse.getPartition_id(), parse.getNumerator(), parse.getDenominator());
            return null;
        } else {
            MsgType msgType = MsgTransfer.CHAR_2_MSG_TYPE($0);
            if (MsgType.AC.equals(msgType)) {

                //认证消息解析
                return AC_Disassembly($3, socketChannel);
            } else if (MsgType.HEART_BEAT.equals(msgType)) {

                //心跳消息解析
                return HEART_BEAT_Disassembly($0, $1, $2, $3, socketChannel);
            } else if (MsgType.NORM_MSG.equals(msgType)) {

                //普通消息解析
                return NORM_MSG_Disassembly($0, $1, $2, $3, socketChannel);
            } else if (MsgType.ERR_MSG.equals(msgType)) {
                //异常消息解析
                return ERR_MSG_Disassembly($0, $1, $2, $3, socketChannel);
            } else {
                //普通消息解析
                return NORM_MSG_Disassembly($0, $1, $2, $3, socketChannel);
            }
        }
    }


    /**
     * @param msg_length  消息长度
     * @param inputStream 流
     * @description 认证消息解析
     */
    private static QyMsg AC_Disassembly(String msg_length, InputStream inputStream) {
        byte[] bytes = IoUtil.readBytes(inputStream, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX));
        return JSON.parseObject(bytes, QyMsg.class);
    }

    /**
     * 心跳消息组装
     */
    private static QyMsg HEART_BEAT_Disassembly(char msg_type, char data_type, char segmentation, String msg_length, InputStream inputStream) {
        QyMsg qyMsg = new QyMsg(MsgTransfer.CHAR_2_MSG_TYPE(msg_type), MsgTransfer.CHAR_2_DATA_TYPE(data_type));
        byte[] bytes = IoUtil.readBytes(inputStream, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX));
        qyMsg.setSegmentation(MsgTransfer.SEGMENTATION_2_BOOLEAN(segmentation));
        qyMsg.setFrom(new String(bytes, StandardCharsets.UTF_8));
        return qyMsg;
    }

    /**
     * 常规消息组装
     */
    private static QyMsg NORM_MSG_Disassembly(char msg_type, char data_type, char segmentation, String msg_length, InputStream inputStream) throws IOException, ClassNotFoundException {

        if (DataType.JSON.equals(MsgTransfer.CHAR_2_DATA_TYPE(data_type))) {

            byte[] bytes = IoUtil.readBytes(inputStream, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX));
            return JSON.parseObject(bytes, QyMsg.class);
        } else if (DataType.OBJECT.equals(MsgTransfer.CHAR_2_DATA_TYPE(data_type))) {

            byte[] bytes = IoUtil.readBytes(inputStream, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX));
            return IoUtil.deserializationObj(bytes, QyMsg.class);
        } else if (DataType.STRING.equals(MsgTransfer.CHAR_2_DATA_TYPE(data_type))) {

            byte[] bytes = IoUtil.readBytes(inputStream, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX));
            String s = new String(bytes, StandardCharsets.UTF_8);
            String from = s.substring(0, 32);
            String msg = s.substring(32);
            QyMsg qyMsg = new QyMsg(MsgTransfer.CHAR_2_MSG_TYPE(msg_type), MsgTransfer.CHAR_2_DATA_TYPE(data_type));
            qyMsg.setFrom(from);
            qyMsg.putMsg(msg.getBytes(StandardCharsets.UTF_8));

            return qyMsg;
        } else if (DataType.STREAM.equals(MsgTransfer.CHAR_2_DATA_TYPE(data_type))) {
            return streamDeal(msg_type, data_type, msg_length, inputStream);
        } else {
            return streamDeal(msg_type, data_type, msg_length, inputStream);
        }
    }

    /**
     * 异常消息组装
     */
    private static QyMsg ERR_MSG_Disassembly(char msg_type, char data_type, char segmentation, String msg_length, InputStream inputStream) {

        if (DataType.JSON.equals(MsgTransfer.CHAR_2_DATA_TYPE(data_type))) {
            byte[] bytes = IoUtil.readBytes(inputStream, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX));
            return JSON.parseObject(bytes, QyMsg.class);
        } else {
            return streamDeal(msg_type, data_type, msg_length, inputStream);
        }
    }

    /**
     * @author YYJ
     * @description 流类型数据处理
     */
    @NotNull
    private static QyMsg streamDeal(char msg_type, char data_type, String msg_length, InputStream inputStream) {
        QyMsg qyMsg = new QyMsg(MsgTransfer.CHAR_2_MSG_TYPE(msg_type), MsgTransfer.CHAR_2_DATA_TYPE(data_type));
        byte[] bytes = IoUtil.readBytes(inputStream, 36);
        qyMsg.setFrom(new String(bytes, StandardCharsets.UTF_8));
        qyMsg.putMsg(IoUtil.readBytes(inputStream, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX) - 36));
        return qyMsg;
    }

    /**
     * @param msg_length    消息长度
     * @param socketChannel 流
     * @description 认证消息解析
     */
    private static QyMsg AC_Disassembly(String msg_length, SocketChannel socketChannel) throws IOException {
        byte[] bytes = IoUtil.readBytes(socketChannel, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX));
        return JSON.parseObject(bytes, QyMsg.class);
    }

    /**
     * 心跳消息组装
     */
    private static QyMsg HEART_BEAT_Disassembly(char msg_type, char data_type, char segmentation, String msg_length, SocketChannel socketChannel) throws IOException {
        byte[] bytes = IoUtil.readBytes(socketChannel, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX));
        QyMsg qyMsg = new QyMsg(MsgTransfer.CHAR_2_MSG_TYPE(msg_type), MsgTransfer.CHAR_2_DATA_TYPE(data_type));
        qyMsg.setSegmentation(MsgTransfer.SEGMENTATION_2_BOOLEAN(segmentation));
        qyMsg.setFrom(new String(bytes, StandardCharsets.UTF_8));
        return qyMsg;
    }

    /**
     * 常规消息组装
     */
    private static QyMsg NORM_MSG_Disassembly(char msg_type, char data_type, char segmentation, String msg_length, SocketChannel socketChannel) throws IOException, ClassNotFoundException {

        if (DataType.JSON.equals(MsgTransfer.CHAR_2_DATA_TYPE(data_type))) {

            byte[] bytes = IoUtil.readBytes(socketChannel, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX));
            return JSON.parseObject(bytes, QyMsg.class);
        } else if (DataType.OBJECT.equals(MsgTransfer.CHAR_2_DATA_TYPE(data_type))) {

            byte[] bytes = IoUtil.readBytes(socketChannel, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX));
            return IoUtil.deserializationObj(bytes, QyMsg.class);
        } else if (DataType.STRING.equals(MsgTransfer.CHAR_2_DATA_TYPE(data_type))) {

            byte[] bytes = IoUtil.readBytes(socketChannel, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX));
            String s = new String(bytes, StandardCharsets.UTF_8);
            String from = s.substring(0, 32);
            String msg = s.substring(32);
            QyMsg qyMsg = new QyMsg(MsgTransfer.CHAR_2_MSG_TYPE(msg_type), MsgTransfer.CHAR_2_DATA_TYPE(data_type));
            qyMsg.setFrom(from);
            qyMsg.putMsg(msg.getBytes(StandardCharsets.UTF_8));

            return qyMsg;
        } else if (DataType.STREAM.equals(MsgTransfer.CHAR_2_DATA_TYPE(data_type))) {
            return streamDeal(msg_type, data_type, msg_length, socketChannel);
        } else {
            return streamDeal(msg_type, data_type, msg_length, socketChannel);
        }
    }

    /**
     * 异常消息组装
     */
    private static QyMsg ERR_MSG_Disassembly(char msg_type, char data_type, char segmentation, String msg_length, SocketChannel socketChannel) throws IOException {

        if (DataType.JSON.equals(MsgTransfer.CHAR_2_DATA_TYPE(data_type))) {
            byte[] bytes = IoUtil.readBytes(socketChannel, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX));
            return JSON.parseObject(bytes, QyMsg.class);
        } else {
            return streamDeal(msg_type, data_type, msg_length, socketChannel);
        }
    }

    /**
     * @author YYJ
     * @description 流类型数据处理
     */
    @NotNull
    private static QyMsg streamDeal(char msg_type, char data_type, String msg_length, SocketChannel socketChannel) throws IOException {
        byte[] bytes = IoUtil.readBytes(socketChannel, 36);
        QyMsg qyMsg = new QyMsg(MsgTransfer.CHAR_2_MSG_TYPE(msg_type), MsgTransfer.CHAR_2_DATA_TYPE(data_type));
        qyMsg.setFrom(new String(bytes, StandardCharsets.UTF_8));
        qyMsg.putMsg(IoUtil.readBytes(socketChannel, Integer.parseInt(msg_length, MsgTransfer.MSG_LENGTH_RADIX) - 36));
        return qyMsg;
    }
}
