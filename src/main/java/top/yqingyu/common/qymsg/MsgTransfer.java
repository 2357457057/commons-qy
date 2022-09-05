package top.yqingyu.common.qymsg;

import cn.hutool.core.io.IORuntimeException;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import top.yqingyu.common.utils.ArrayUtil;
import top.yqingyu.common.utils.IoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.qymsg.MsgTransfer
 * @description
 * @createTime 2022年09月01日 23:03:00
 */
@Slf4j
@SuppressWarnings("all")
public class MsgTransfer {
    //消息长度占位长度
    private static final int BODY_LENGTH_LENGTH = 5;
    private static int BODY_LENGTH_MAX = 1024 * 1;

//    private static final int MSG_LENGTH_MAX = 33_554_432;

    private static int MSG_LENGTH_RADIX = 0;

    private static ExecutorService IO_POOL = null;

    private static Hashtable<DataType, Character> DATA_TYPE_2_CHAR;
    private static Hashtable<Character, DataType> CHAR_2_DATA_TYPE;

    private static Hashtable<MsgType, Character> MSG_TYPE_2_CHAR;
    private static Hashtable<Character, MsgType> CHAR_2_MSG_TYPE;
    private static Hashtable<Character, Boolean> SEGMENTATION_2_BOOLEAN;
    private static Hashtable<Boolean, Character> BOOLEAN_2_SEGMENTATION;


    private static final String DICT = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";

    static {
        //消息类型映射
        {
            MSG_TYPE_2_CHAR = new Hashtable<>();
            MSG_TYPE_2_CHAR.put(MsgType.AC, '#');
            MSG_TYPE_2_CHAR.put(MsgType.HEART_BEAT, '&');
            MSG_TYPE_2_CHAR.put(MsgType.NORM_MSG, '%');
            MSG_TYPE_2_CHAR.put(MsgType.ERR_MSG, '=');

            CHAR_2_MSG_TYPE = new Hashtable<>();
            CHAR_2_MSG_TYPE.put('#', MsgType.AC);
            CHAR_2_MSG_TYPE.put('&', MsgType.HEART_BEAT);
            CHAR_2_MSG_TYPE.put('%', MsgType.NORM_MSG);
            CHAR_2_MSG_TYPE.put('=', MsgType.ERR_MSG);

        }
        //数据类型映射
        {
            DATA_TYPE_2_CHAR = new Hashtable<>();
            DATA_TYPE_2_CHAR.put(DataType.OBJECT, '=');
            DATA_TYPE_2_CHAR.put(DataType.JSON, '%');
            DATA_TYPE_2_CHAR.put(DataType.STRING, '&');
            DATA_TYPE_2_CHAR.put(DataType.STREAM, '#');

            CHAR_2_DATA_TYPE = new Hashtable<>();
            CHAR_2_DATA_TYPE.put('=', DataType.OBJECT);
            CHAR_2_DATA_TYPE.put('%', DataType.JSON);
            CHAR_2_DATA_TYPE.put('&', DataType.STRING);
            CHAR_2_DATA_TYPE.put('#', DataType.STREAM);
        }
        //消息分片映射
        {
            SEGMENTATION_2_BOOLEAN = new Hashtable<>();
            SEGMENTATION_2_BOOLEAN.put('+', true);
            SEGMENTATION_2_BOOLEAN.put('-', false);

            BOOLEAN_2_SEGMENTATION = new Hashtable<>();
            BOOLEAN_2_SEGMENTATION.put(true, '+');
            BOOLEAN_2_SEGMENTATION.put(false, '-');

        }
    }


    public static void init(int radix, int body_length_max, ExecutorService pool) {
        MSG_LENGTH_RADIX = radix;
        BODY_LENGTH_MAX = body_length_max;
        IO_POOL = pool;

    }

    private static char DATA_TYPE_2_CHAR(DataType dataType) {
        return DATA_TYPE_2_CHAR.get(dataType);
    }

    private static DataType CHAR_2_DATA_TYPE(char c) {
        return CHAR_2_DATA_TYPE.get(c);
    }

    private static char MSG_TYPE_2_CHAR(MsgType msgType) {
        return MSG_TYPE_2_CHAR.get(msgType);
    }

    private static MsgType CHAR_2_MSG_TYPE(char c) {
        return CHAR_2_MSG_TYPE.get(c);
    }

    private static boolean SEGMENTATION_2_BOOLEAN(char c) {

        return SEGMENTATION_2_BOOLEAN.get(c);
    }

    private static char BOOLEAN_2_SEGMENTATION(boolean b) {
        return BOOLEAN_2_SEGMENTATION.get(b);
    }


    private static byte[] getQyMsgBytes(byte[]... bytess) {
        byte[] buf = new byte[0];

        for (byte[] bytes : bytess) {
            StringBuilder msgLength = new StringBuilder();
            msgLength.append(Integer.toUnsignedString(bytes.length, MSG_LENGTH_RADIX));

            //长度信息不足MSG_LENGTH_LENGTH位按0补充
            while (msgLength.toString().getBytes(StandardCharsets.UTF_8).length != BODY_LENGTH_LENGTH) {
                msgLength.insert(0, '0');
            }
            buf = ArrayUtils.addAll(buf, msgLength.toString().getBytes(StandardCharsets.UTF_8));
            buf = ArrayUtils.addAll(buf, bytes);
        }
        //将信息长度与信息组合
        return buf;
    }

    private static String getLength(byte[] bytes) {
        StringBuilder msgLength = new StringBuilder();
        msgLength.append(Integer.toUnsignedString(bytes.length, MSG_LENGTH_RADIX));
        //长度信息不足MSG_LENGTH_LENGTH位按0补充
        while (msgLength.toString().getBytes(StandardCharsets.UTF_8).length != BODY_LENGTH_LENGTH) {
            msgLength.insert(0, '0');
        }
        //将信息长度与信息组合
        return msgLength.toString();
    }


    public static void writeQyMsg(SocketChannel socketChannel, QyMsg qyMsg) throws Exception {
        ArrayList<byte[]> assembly = assembly(qyMsg);
        for (byte[] bytes : assembly) {
            writeBytes(socketChannel, bytes);
        }
    }

    public static void writeQyMsg(Socket socket, QyMsg qyMsg) throws Exception {
        ArrayList<byte[]> assembly = assembly(qyMsg);
        for (byte[] bytes : assembly) {
            writeBytes(socket, bytes);
        }
    }


    /**
     * @param qyMsg 消息
     * @author YYJ
     * @description 消息组装
     */
    public static ArrayList<byte[]> assembly(QyMsg qyMsg) throws IOException {
        ArrayList<byte[]> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(MSG_TYPE_2_CHAR(qyMsg.getMsgType()));
        sb.append(DATA_TYPE_2_CHAR(qyMsg.getDataType()));

        if (MsgType.AC.equals(qyMsg.getMsgType())) {

            //认证消息组装
            AC_Assembly(sb, qyMsg, list);
        } else if (MsgType.HEART_BEAT.equals(qyMsg.getMsgType())) {

            //心跳消息组装
            HEART_BEAT_Assembly(sb, qyMsg, list);
        } else if (MsgType.NORM_MSG.equals(qyMsg.getMsgType())) {

            //普通消息组装
            NORM_MSG_Assembly(sb, qyMsg, list);
        } else if (MsgType.ERR_MSG.equals(qyMsg.getMsgType())) {

            ERR_MSG_Assembly(sb, qyMsg, list);
        } else {

            //普通消息组装
            NORM_MSG_Assembly(sb, qyMsg, list);
        }
        //将信息长度与信息组合
        return list;
    }


    /**
     * @author YYJ
     * @description 消息解析
     */
    public static QyMsg disassembly(Socket socket, Queue<QyMsg> segmentation$queue) throws IOException, ClassNotFoundException {
        InputStream inputStream = socket.getInputStream();
        byte[] readBytes = IoUtil.readBytes(inputStream, 8);
        String s = new String(readBytes, StandardCharsets.UTF_8);
        char $0 = s.charAt(0);//msg  type
        char $1 = s.charAt(1);//data type
        char $2 = s.charAt(2);//是否分片
        String $3 = s.substring(4);     //后五位


        if (SEGMENTATION_2_BOOLEAN($2)) {
            QyMsg parse = new QyMsg(CHAR_2_MSG_TYPE($0), CHAR_2_DATA_TYPE($1));
            parse.setSegmentation(SEGMENTATION_2_BOOLEAN($2));
            readBytes = IoUtil.readBytes(inputStream, 18);
            String partition_numerator_denominator = new String(readBytes, StandardCharsets.UTF_8);
            parse.setPartition_id(partition_numerator_denominator.substring(0, 16));
            parse.setNumerator(Integer.parseInt(partition_numerator_denominator.substring(16, 17), MSG_LENGTH_RADIX));
            parse.setDenominator(Integer.parseInt(partition_numerator_denominator.substring(17, 18), MSG_LENGTH_RADIX));
            readBytes = IoUtil.readBytes(inputStream, Integer.parseInt($3, MSG_LENGTH_RADIX));
            parse.putMsg(readBytes);
            segmentation$queue.add(parse);
            return null;
        } else {
            MsgType msgType = CHAR_2_MSG_TYPE($0);
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
    public static QyMsg disassembly(SocketChannel socketChannel, Queue<QyMsg> segmentation$queue) throws IOException, ClassNotFoundException {

        byte[] readBytes = IoUtil.readBytes(socketChannel, 8);
        String s = new String(readBytes, StandardCharsets.UTF_8);
        char $0 = s.charAt(0);//msg  type
        char $1 = s.charAt(1);//data type
        char $2 = s.charAt(2);//是否分片
        String $3 = s.substring(4);     //后五位


        if (SEGMENTATION_2_BOOLEAN($2)) {
            QyMsg parse = new QyMsg(CHAR_2_MSG_TYPE($0), CHAR_2_DATA_TYPE($1));
            parse.setSegmentation(SEGMENTATION_2_BOOLEAN($2));
            readBytes = IoUtil.readBytes(socketChannel, 18);
            String partition_numerator_denominator = new String(readBytes, StandardCharsets.UTF_8);
            parse.setPartition_id(partition_numerator_denominator.substring(0, 16));
            parse.setNumerator(Integer.parseInt(partition_numerator_denominator.substring(16, 17), MSG_LENGTH_RADIX));
            parse.setDenominator(Integer.parseInt(partition_numerator_denominator.substring(17, 18), MSG_LENGTH_RADIX));
            readBytes = IoUtil.readBytes(socketChannel, Integer.parseInt($3, MSG_LENGTH_RADIX));
            parse.putMsg(readBytes);
            segmentation$queue.add(parse);
            return null;
        } else {
            MsgType msgType = CHAR_2_MSG_TYPE($0);
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
     * 认证消息组装
     *
     * @param qyMsg 消息体对象
     * @param sb    消息头SB
     * @param list  返回的消息集合
     */
    private static void AC_Assembly(StringBuilder sb, QyMsg qyMsg, ArrayList<byte[]> list) {
        sb.setCharAt(1, DATA_TYPE_2_CHAR(DataType.JSON));
        sb.append(BOOLEAN_2_SEGMENTATION(false));
        byte[] body = JSON.toJSONBytes(qyMsg);
        sb.append(getLength(body));
        byte[] header = sb.toString().getBytes(StandardCharsets.UTF_8);
        list.add(ArrayUtils.addAll(header, body));
    }

    /**
     * 心跳消息组装
     *
     * @param qyMsg 消息体对象
     * @param sb    消息头SB
     * @param list  返回的消息集合
     */
    private static void HEART_BEAT_Assembly(StringBuilder sb, QyMsg qyMsg, ArrayList<byte[]> list) {
        sb.setCharAt(1, DATA_TYPE_2_CHAR(DataType.STRING));
        sb.append(BOOLEAN_2_SEGMENTATION(false));
        byte[] body = qyMsg.getFrom().getBytes(StandardCharsets.UTF_8);
        sb.append(getLength(body));
        byte[] header = sb.toString().getBytes(StandardCharsets.UTF_8);
        list.add(ArrayUtils.addAll(header, body));
    }

    /**
     * 常规消息组装
     *
     * @param qyMsg 消息体对象
     * @param sb    消息头SB
     * @param list  返回的消息集合
     */
    private static void NORM_MSG_Assembly(StringBuilder sb, QyMsg qyMsg, ArrayList<byte[]> list) throws IOException {

        if (DataType.JSON.equals(qyMsg.getDataType())) {

            byte[] body = JSON.toJSONString(qyMsg).getBytes(StandardCharsets.UTF_8);
            OUT_OF_LENGTH_MSG_Assembly(body, sb, list);
        } else if (DataType.OBJECT.equals(qyMsg.getDataType())) {

            byte[] body = IoUtil.objToSerializBytes(qyMsg);
            OUT_OF_LENGTH_MSG_Assembly(body, sb, list);
        } else if (DataType.STRING.equals(qyMsg.getDataType())) {

            byte[] body = (qyMsg.getFrom() + MsgHelper.gainMsg(qyMsg)).getBytes(StandardCharsets.UTF_8);
            OUT_OF_LENGTH_MSG_Assembly(body, sb, list);
        } else if (DataType.STREAM.equals(qyMsg.getDataType())) {

            byte[] bytes = qyMsg.getFrom().getBytes(StandardCharsets.UTF_8);
            OUT_OF_LENGTH_MSG_Assembly(ArrayUtils.addAll(bytes, (byte[]) MsgHelper.gainObjMsg(qyMsg)), sb, list);
        } else {
            byte[] bytes = qyMsg.getFrom().getBytes(StandardCharsets.UTF_8);
            OUT_OF_LENGTH_MSG_Assembly(ArrayUtils.addAll(bytes, (byte[]) MsgHelper.gainObjMsg(qyMsg)), sb, list);
        }
    }

    /**
     * 异常消息组装
     *
     * @param qyMsg 消息体对象
     * @param sb    消息头SB
     * @param list  返回的消息集合
     */
    private static void ERR_MSG_Assembly(StringBuilder sb, QyMsg qyMsg, ArrayList<byte[]> list) {
        if (DataType.JSON.equals(qyMsg.getDataType())) {

            byte[] body = JSON.toJSONString(qyMsg).getBytes(StandardCharsets.UTF_8);
            OUT_OF_LENGTH_MSG_Assembly(body, sb, list);
        } else {
            byte[] body = (qyMsg.getFrom() + MsgHelper.gainMsg(qyMsg)).getBytes(StandardCharsets.UTF_8);
            OUT_OF_LENGTH_MSG_Assembly(body, sb, list);
        }
    }

    /**
     * 对消息长度的判断  拆分并组装
     *
     * @param body 原消息体byte
     * @param sb   消息头SB
     * @param list 返回的消息集合
     */
    private static void OUT_OF_LENGTH_MSG_Assembly(byte[] body, StringBuilder sb, ArrayList<byte[]> list) {
        ArrayList<byte[]> bodyList = ArrayUtil.checkArrayLength(body, BODY_LENGTH_MAX);
        if (bodyList.size() == 1) {
            sb.append(BOOLEAN_2_SEGMENTATION(false));
            sb.append(getLength(body));
            byte[] header = sb.toString().getBytes(StandardCharsets.UTF_8);
            list.add(ArrayUtils.addAll(header, body));
        } else {
            sb.append(BOOLEAN_2_SEGMENTATION.get(true));
            String part_trade_id = RandomStringUtils.random(16, DICT);
            for (int i = 1; i <= bodyList.size(); i++) {
                StringBuilder builder = new StringBuilder(sb);
                byte[] cBody = bodyList.get(i - 1);
                builder.append(getLength(cBody));
                builder.append(part_trade_id);
                builder.append(Integer.toUnsignedString(i));
                builder.append(Integer.toUnsignedString(bodyList.size()));
                byte[] cHeader = builder.toString().getBytes(StandardCharsets.UTF_8);
                list.add(ArrayUtils.addAll(cHeader, cBody));
            }
        }
    }


    /**
     * @param msg_length  消息长度
     * @param inputStream 流
     * @description 认证消息解析
     */
    private static QyMsg AC_Disassembly(String msg_length, InputStream inputStream) {
        byte[] bytes = IoUtil.readBytes(inputStream, Integer.parseInt(msg_length, MSG_LENGTH_RADIX));
        return JSON.parseObject(bytes, QyMsg.class);
    }

    /**
     * 心跳消息组装
     */
    private static QyMsg HEART_BEAT_Disassembly(char msg_type, char data_type, char segmentation, String msg_length, InputStream inputStream) {
        QyMsg qyMsg = new QyMsg(CHAR_2_MSG_TYPE(msg_type), CHAR_2_DATA_TYPE(data_type));
        byte[] bytes = IoUtil.readBytes(inputStream, Integer.parseInt(msg_length, MSG_LENGTH_RADIX));
        qyMsg.setSegmentation(SEGMENTATION_2_BOOLEAN(segmentation));
        qyMsg.setFrom(new String(bytes, StandardCharsets.UTF_8));
        return qyMsg;
    }

    /**
     * 常规消息组装
     */
    private static QyMsg NORM_MSG_Disassembly(char msg_type, char data_type, char segmentation, String msg_length, InputStream inputStream) throws IOException, ClassNotFoundException {

        if (DataType.JSON.equals(CHAR_2_DATA_TYPE(data_type))) {

            byte[] bytes = IoUtil.readBytes(inputStream, Integer.parseInt(msg_length, MSG_LENGTH_RADIX));
            return JSON.parseObject(bytes, QyMsg.class);
        } else if (DataType.OBJECT.equals(CHAR_2_DATA_TYPE(data_type))) {

            byte[] bytes = IoUtil.readBytes(inputStream, Integer.parseInt(msg_length, MSG_LENGTH_RADIX));
            return IoUtil.deserializationObj(bytes, QyMsg.class);
        } else if (DataType.STRING.equals(CHAR_2_DATA_TYPE(data_type))) {

            byte[] bytes = IoUtil.readBytes(inputStream, Integer.parseInt(msg_length, MSG_LENGTH_RADIX));
            String s = new String(bytes, StandardCharsets.UTF_8);
            String from = s.substring(0, 32);
            String msg = s.substring(32);
            QyMsg qyMsg = new QyMsg(CHAR_2_MSG_TYPE(msg_type), CHAR_2_DATA_TYPE(data_type));
            qyMsg.setFrom(from);
            qyMsg.putMsg(msg);

            return qyMsg;
        } else if (DataType.STREAM.equals(CHAR_2_DATA_TYPE(data_type))) {
            return streamDeal(msg_type, data_type, msg_length, inputStream);
        } else {
            return streamDeal(msg_type, data_type, msg_length, inputStream);
        }
    }

    /**
     * 异常消息组装
     */
    private static QyMsg ERR_MSG_Disassembly(char msg_type, char data_type, char segmentation, String msg_length, InputStream inputStream) {

        if (DataType.JSON.equals(CHAR_2_DATA_TYPE(data_type))) {
            byte[] bytes = IoUtil.readBytes(inputStream, Integer.parseInt(msg_length, MSG_LENGTH_RADIX));
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
        QyMsg qyMsg = new QyMsg(CHAR_2_MSG_TYPE(msg_type), CHAR_2_DATA_TYPE(data_type));
        byte[] bytes = IoUtil.readBytes(inputStream, 36);
        qyMsg.setFrom(new String(bytes, StandardCharsets.UTF_8));
        qyMsg.putMsg(IoUtil.readBytes(inputStream, Integer.parseInt(msg_length, MSG_LENGTH_RADIX) - 36));
        return qyMsg;
    }

    /**
     * @param msg_length    消息长度
     * @param socketChannel 流
     * @description 认证消息解析
     */
    private static QyMsg AC_Disassembly(String msg_length, SocketChannel socketChannel) throws IOException {
        byte[] bytes = IoUtil.readBytes(socketChannel, Integer.parseInt(msg_length, MSG_LENGTH_RADIX));
        return JSON.parseObject(bytes, QyMsg.class);
    }

    /**
     * 心跳消息组装
     */
    private static QyMsg HEART_BEAT_Disassembly(char msg_type, char data_type, char segmentation, String msg_length, SocketChannel socketChannel) throws IOException {
        byte[] bytes = IoUtil.readBytes(socketChannel, Integer.parseInt(msg_length, MSG_LENGTH_RADIX));
        QyMsg qyMsg = new QyMsg(CHAR_2_MSG_TYPE(msg_type), CHAR_2_DATA_TYPE(data_type));
        qyMsg.setSegmentation(SEGMENTATION_2_BOOLEAN(segmentation));
        qyMsg.setFrom(new String(bytes, StandardCharsets.UTF_8));
        return qyMsg;
    }

    /**
     * 常规消息组装
     */
    private static QyMsg NORM_MSG_Disassembly(char msg_type, char data_type, char segmentation, String msg_length, SocketChannel socketChannel) throws IOException, ClassNotFoundException {

        if (DataType.JSON.equals(CHAR_2_DATA_TYPE(data_type))) {

            byte[] bytes = IoUtil.readBytes(socketChannel, Integer.parseInt(msg_length, MSG_LENGTH_RADIX));
            return JSON.parseObject(bytes, QyMsg.class);
        } else if (DataType.OBJECT.equals(CHAR_2_DATA_TYPE(data_type))) {

            byte[] bytes = IoUtil.readBytes(socketChannel, Integer.parseInt(msg_length, MSG_LENGTH_RADIX));
            return IoUtil.deserializationObj(bytes, QyMsg.class);
        } else if (DataType.STRING.equals(CHAR_2_DATA_TYPE(data_type))) {

            byte[] bytes = IoUtil.readBytes(socketChannel, Integer.parseInt(msg_length, MSG_LENGTH_RADIX));
            String s = new String(bytes, StandardCharsets.UTF_8);
            String from = s.substring(0, 32);
            String msg = s.substring(32);
            QyMsg qyMsg = new QyMsg(CHAR_2_MSG_TYPE(msg_type), CHAR_2_DATA_TYPE(data_type));
            qyMsg.setFrom(from);
            qyMsg.putMsg(msg);

            return qyMsg;
        } else if (DataType.STREAM.equals(CHAR_2_DATA_TYPE(data_type))) {
            return streamDeal(msg_type, data_type, msg_length, socketChannel);
        } else {
            return streamDeal(msg_type, data_type, msg_length, socketChannel);
        }
    }

    /**
     * 异常消息组装
     */
    private static QyMsg ERR_MSG_Disassembly(char msg_type, char data_type, char segmentation, String msg_length, SocketChannel socketChannel) throws IOException {

        if (DataType.JSON.equals(CHAR_2_DATA_TYPE(data_type))) {
            byte[] bytes = IoUtil.readBytes(socketChannel, Integer.parseInt(msg_length, MSG_LENGTH_RADIX));
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
        QyMsg qyMsg = new QyMsg(CHAR_2_MSG_TYPE(msg_type), CHAR_2_DATA_TYPE(data_type));
        qyMsg.setFrom(new String(bytes, StandardCharsets.UTF_8));
        qyMsg.putMsg(IoUtil.readBytes(socketChannel, Integer.parseInt(msg_length, MSG_LENGTH_RADIX) - 36));
        return qyMsg;
    }

    public static void writeMessage(SocketChannel socketChannel, String userId, String msg) throws Exception {
        writeQyBytes(socketChannel, getQyMsgBytes(userId.getBytes(StandardCharsets.UTF_8), msg.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * description: 通过 SocketChannel 写出杨氏消息体
     *
     * @author yqingyu
     * DATE 2022/4/22
     */
    public static void writeMessage(SocketChannel socketChannel, String msg) throws Exception {
        try {
            writeQyBytes(socketChannel, msg.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new Exception("WriteMsgError", e);
        }
    }

//    /**
//     * description: 通过 SocketChannel 写出杨氏消息体
//     *
//     * @author yqingyu
//     * DATE 2022/4/22
//     */
//    public static void writeMessage(SocketChannel socketChannel, QyMsg msg) throws Exception {
//        try {
//            writeQyBytes(socketChannel, msg.toString().getBytes(StandardCharsets.UTF_8));
//        } catch (Exception e) {
//            throw new Exception("WriteMsgError", e);
//        }
//    }


    public static void writeQyBytes(SocketChannel socketChannel, byte[] bytes) throws Exception {

        bytes = getQyMsgBytes(bytes);

        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
    }

    public static void writeBytes(SocketChannel socketChannel, byte[] bytes) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
    }

    public static void writeBytes(Socket socket, byte[] bytes) throws Exception {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
    }


    /**
     * 写出带有用户名的信息
     * date 2022/5/7 1:12
     * return void
     */
    public static void writeMessage(Socket socket, QyMsg msg) throws Exception {


        byte[] bytes = JSON.toJSONBytes(msg);

        byte[] qyMsgBytes = getQyMsgBytes(bytes);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(qyMsgBytes);
        outputStream.flush();
    }


    public static QyMsg readMsg(Socket socket) throws Exception {
        byte[] bytes = readQyBytes(socket);
        return JSON.parseObject(bytes, QyMsg.class);
    }

    public static QyMsg readMsg(Socket socket, int timeout) throws Exception {
        byte[] bytes = readQyBytes(socket, timeout);
        return JSON.parseObject(bytes, QyMsg.class);
    }


    /**
     * description: 通过 Socket 写出杨氏消息体
     *
     * @author yqingyu
     * DATE 2022/4/22
     */
    public static void writeMessage(Socket socket, String msg) throws Exception {
        writeQyBytes(socket, msg.getBytes(StandardCharsets.UTF_8));
    }


    public static void writeQyBytes(Socket socket, byte[] bytes) throws Exception {

        OutputStream outputStream = socket.getOutputStream();

        bytes = getQyMsgBytes(bytes);

        outputStream.write(bytes);
        outputStream.flush();
    }


    public static QyMsg readMessage2(SocketChannel socketChannel) throws IOException {

        QyMsg qyMsgHeader = JSON.parseObject(readQyBytes(socketChannel), QyMsg.class);
        return qyMsgHeader;
    }

    /**
     * description: 读取 SocketChannel 中的杨氏消息体
     *
     * @author yqingyu
     * DATE 2022/4/22
     */
    public static String readMessage(SocketChannel socketChannel) throws IOException {

        try {
            return new String(readQyBytes(socketChannel), StandardCharsets.UTF_8);
        } catch (Exception e) {
            socketChannel.close();
            e.printStackTrace();
            throw e;
        }
    }

    public static byte[] readQyBytes(SocketChannel socketChannel) throws IOException {
        String len = new String(IoUtil.readBytes(socketChannel, BODY_LENGTH_LENGTH), StandardCharsets.UTF_8);

        return IoUtil.readBytes(socketChannel, Integer.parseInt(len, MSG_LENGTH_RADIX));
    }


    /**
     * description: 读取 Socket 中的杨氏消息体
     *
     * @author yqingyu
     * DATE 2022/4/22
     */
    public static String readMessage(Socket socket) throws IOException {
        String msg;
        try {
            msg = new String(readQyBytes(socket), StandardCharsets.UTF_8);
        } catch (IORuntimeException e) {
            msg = "";
            log.error("消息读取异常 ", e);
        }
        return msg;
    }

    public static byte[] readQyBytes(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        byte[] buff = IoUtil.readBytes(inputStream, BODY_LENGTH_LENGTH);
        String msgLength = new String(buff, StandardCharsets.UTF_8);

        buff = IoUtil.readBytes(inputStream, Integer.parseInt(msgLength, MSG_LENGTH_RADIX));
        return buff;
    }


    /**
     * description: 读取 Socket 中的杨氏消息体
     *
     * @author yqingyu
     * DATE 2022/4/22
     */
    public static String readMessage(Socket socket, int timeout) throws Exception {
        String msg;
        try {
            msg = new String(readQyBytes(socket, timeout), StandardCharsets.UTF_8);
        } catch (IORuntimeException e) {
            msg = "";
            e.printStackTrace();
            throw e;
        }
        return msg;
    }

    public static byte[] readQyBytes(Socket socket, int timeout) throws Exception {
        InputStream inputStream = socket.getInputStream();
        byte[] buff = IoUtil.readBytes(inputStream, BODY_LENGTH_LENGTH);
        String msgLength = new String(buff, StandardCharsets.UTF_8);

        buff = IoUtil.readBytes(inputStream, Integer.parseInt(msgLength, MSG_LENGTH_RADIX), timeout);
        return buff;
    }

    /**
     * 序列化写出
     *
     * @author YYJ
     * @version 1.0.0
     * @description
     */
    public static void writeSerializable(SocketChannel socketChannel, QyMsg qyQyMsg) throws Exception {
        writeQyBytes(socketChannel, IoUtil.objToSerializBytes(qyQyMsg));
    }

    /**
     * 序列化写出
     *
     * @author YYJ
     * @version 1.0.0
     * @description
     */
    public static void writeSerializable$(Socket socket, QyMsg qyQyMsg) throws Exception {
        writeQyBytes(socket, IoUtil.objToSerializBytes(qyQyMsg));
    }


    /**
     * 序列化读取
     *
     * @author YYJ
     * @version 1.0.0
     * @description
     */
    public static QyMsg readSerializable(SocketChannel socketChannel) throws Exception {
        return IoUtil.deserializationObj(readQyBytes(socketChannel), QyMsg.class);
    }

    /**
     * 序列化读取
     *
     * @author YYJ
     * @version 1.0.0
     * @description
     */
    public static QyMsg readSerializable(Socket socket) throws Exception {
        return IoUtil.deserializationObj(readQyBytes(socket), QyMsg.class);
    }

}
