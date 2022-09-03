package top.yqingyu.common.qymsg;

import cn.hutool.core.io.IORuntimeException;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import top.yqingyu.common.utils.ArrayUtil;
import top.yqingyu.common.utils.IoUtil;
import top.yqingyu.common.utils.ThreadUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.qymsg.MsgTransfer
 * @description
 * @createTime 2022年09月01日 23:03:00
 */
@Slf4j
public class MsgTransfer {
    //消息长度占位长度
    private static final int BODY_LENGTH_LENGTH = 5;

    private static final int MSG_LENGTH_LENGTH = 8;
    private static int MSG_LENGTH_RADIX = 0;

    private static ExecutorService IO_POOL = null;

    private static Hashtable<DataType, String> DATA_TYPE_2_CHAR = null;
    private static Hashtable<String, DataType> CHAR_2_DATA_TYPE = null;

    private static Hashtable<MsgType, String> MSG_TYPE_2_CHAR = null;
    private static Hashtable<String, MsgType> CHAR_2_MSG_TYPE = null;
    private static Hashtable<String, Boolean> SEGMENTATION_2_BOOLEAN = null;
    private static Hashtable<Boolean, String> BOOLEAN_2_SEGMENTATION = null;

    //    private static final int MSG_LENGTH_MAX = 33_554_432;
    private static final int MSG_LENGTH_MAX = 100;

    private static final String DICT = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";


    public static void init(int radix, ExecutorService pool) {
        MSG_LENGTH_RADIX = radix;
        IO_POOL = pool;
        //消息类型映射
        {
            MSG_TYPE_2_CHAR = new Hashtable<>();
            MSG_TYPE_2_CHAR.put(MsgType.AC, "#");
            MSG_TYPE_2_CHAR.put(MsgType.HEART_BEAT, "&");
            MSG_TYPE_2_CHAR.put(MsgType.NORM_MSG, "%");
            MSG_TYPE_2_CHAR.put(MsgType.ERR_MSG, "=");

            CHAR_2_MSG_TYPE = new Hashtable<>();
            CHAR_2_MSG_TYPE.put("#", MsgType.AC);
            CHAR_2_MSG_TYPE.put("&", MsgType.HEART_BEAT);
            CHAR_2_MSG_TYPE.put("%", MsgType.NORM_MSG);
            CHAR_2_MSG_TYPE.put("=", MsgType.ERR_MSG);

        }
        //数据类型映射
        {
            DATA_TYPE_2_CHAR = new Hashtable<>();
            DATA_TYPE_2_CHAR.put(DataType.OBJECT, "=");
            DATA_TYPE_2_CHAR.put(DataType.JSON, "%");
            DATA_TYPE_2_CHAR.put(DataType.STRING, "&");
            DATA_TYPE_2_CHAR.put(DataType.STREAM, "#");

            CHAR_2_DATA_TYPE = new Hashtable<>();
            CHAR_2_DATA_TYPE.put("=", DataType.OBJECT);
            CHAR_2_DATA_TYPE.put("%", DataType.JSON);
            CHAR_2_DATA_TYPE.put("&", DataType.STRING);
            CHAR_2_DATA_TYPE.put("#", DataType.STREAM);
        }
        //消息分片映射
        {
            SEGMENTATION_2_BOOLEAN = new Hashtable<>();
            SEGMENTATION_2_BOOLEAN.put("+", true);
            SEGMENTATION_2_BOOLEAN.put("-", false);

            BOOLEAN_2_SEGMENTATION = new Hashtable<>();
            BOOLEAN_2_SEGMENTATION.put(true, "+");
            BOOLEAN_2_SEGMENTATION.put(false, "-");

        }

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


    /**
     * @param qyMsg
     * @return
     * @author YYJ
     * @version 1.0.0
     * @description 消息组装
     */
    private static ArrayList<byte[]> assemblyQyMsg(QyMsg qyMsg) throws IOException {
        ArrayList<byte[]> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(MSG_TYPE_2_CHAR.get(qyMsg.getMsgType()));
        sb.append(DATA_TYPE_2_CHAR.get(qyMsg.getDataType()));

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
     * 认证消息组装
     *
     * @param qyMsg 消息体对象
     * @param sb    消息头SB
     * @param list  返回的消息集合
     */
    private static void AC_Assembly(StringBuilder sb, QyMsg qyMsg, ArrayList<byte[]> list) {
        sb.replace(1, 2, DATA_TYPE_2_CHAR.get(DataType.JSON));
        sb.append(BOOLEAN_2_SEGMENTATION.get(false));
        byte[] body = JSON.toJSONString(qyMsg).getBytes(StandardCharsets.UTF_8);
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
        sb.replace(1, 2, DATA_TYPE_2_CHAR.get(DataType.STRING));
        sb.append(BOOLEAN_2_SEGMENTATION.get(false));
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
        ArrayList<byte[]> bodyList = ArrayUtil.checkArrayLength(body, MSG_LENGTH_MAX);
        if (bodyList.size() == 1) {
            sb.append(BOOLEAN_2_SEGMENTATION.get(false));
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


    public static void main(String[] args) throws IOException {
        init(32, ThreadUtil.createQyFixedThreadPool(1, null, null));
        QyMsg qyMsg = new QyMsg(MsgType.NORM_MSG, DataType.OBJECT);
        qyMsg.putMsg("ACAC");

        qyMsg.setFrom("小杨");
        ArrayList<byte[]> list = null;
        long l1 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
//            long l = System.currentTimeMillis();
            list = assemblyQyMsg(qyMsg);
//            System.out.println(System.currentTimeMillis() - l);
        }
        System.out.println(System.currentTimeMillis() - l1);

        for (byte[] bytes : list) {
            ArrayList<Integer> stream = new ArrayList<>();
            for (byte b : bytes) {
                stream.add((int) b);
            }
            IoUtil.WriteStreamToInputStream inputStream = new IoUtil.WriteStreamToInputStream(stream);
            byte[] readBytes = IoUtil.readBytes(inputStream, 8);
            System.out.println(new String(readBytes, StandardCharsets.UTF_8));
        }

    }

    public static void writeMessage(SocketChannel socketChannel, String userId, String msg) throws Exception {
        writeQyBytes(
                socketChannel,
                getQyMsgBytes(
                        userId.getBytes(StandardCharsets.UTF_8),
                        msg.getBytes(StandardCharsets.UTF_8)
                ));
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

    /**
     * description: 通过 SocketChannel 写出杨氏消息体
     *
     * @author yqingyu
     * DATE 2022/4/22
     */
    public static void writeMessage(SocketChannel socketChannel, QyMsg msg) throws Exception {
        try {
            writeQyBytes(socketChannel, msg.toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new Exception("WriteMsgError", e);
        }
    }


    public static void writeQyBytes(SocketChannel socketChannel, byte[] bytes) throws Exception {

        bytes = getQyMsgBytes(bytes);

        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
    }


    /**
     * 写出带有用户名的信息
     * date 2022/5/7 1:12
     * return void
     */
    public static void writeMessage(Socket socket, QyMsg msg) throws Exception {


        byte[] bytes = JSON.toJSONBytes(msg);

        byte[] qyMsgBytes = getQyMsgBytes(
                bytes
        );
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
