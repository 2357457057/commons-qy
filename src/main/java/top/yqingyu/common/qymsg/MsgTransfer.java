package top.yqingyu.common.qymsg;

import cn.hutool.core.io.IORuntimeException;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import top.yqingyu.common.utils.IoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
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
    private static final int MSG_LENGTH_LENGTH = 8;

    private static int MSG_LENGTH_RADIX = 0;


    private static ExecutorService IO_POOL = null;


    public static void init(int radix, ExecutorService pool) {
        MSG_LENGTH_RADIX = radix;
        IO_POOL = pool;
    }


    private static byte[] getQyMsgBytes(byte[]... bytess) {
        byte[] buf = new byte[0];

        for (byte[] bytes : bytess) {
            StringBuilder msgLength = new StringBuilder();
            msgLength.append(Integer.toUnsignedString(bytes.length, MSG_LENGTH_RADIX));

            //长度信息不足MSG_LENGTH_LENGTH位按0补充
            while (msgLength.toString().getBytes(StandardCharsets.UTF_8).length != MSG_LENGTH_LENGTH) {
                msgLength.insert(0, '0');
            }
            buf = ArrayUtils.addAll(buf, msgLength.toString().getBytes(StandardCharsets.UTF_8));
            buf = ArrayUtils.addAll(buf, bytes);
        }
        //将信息长度与信息组合
        return buf;
    }


    private static byte[] getQyMsgBytes(QyMsg qyMsg) {
        byte[] buf = new byte[0];

        //将信息长度与信息组合
        return buf;
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
        String len = new String(IoUtil.readBytes(socketChannel, MSG_LENGTH_LENGTH), StandardCharsets.UTF_8);

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
        byte[] buff = IoUtil.readBytes(inputStream, MSG_LENGTH_LENGTH);
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
        byte[] buff = IoUtil.readBytes(inputStream, MSG_LENGTH_LENGTH);
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
