package top.yqingyu.common.utils;

import cn.hutool.core.io.IORuntimeException;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import top.yqingyu.common.qymsg.QyMsgHeader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author YYJ
 * @version 1.0.0
 * @date 2022/4/21 3:43
 * @description
 * @modified by
 */
@Slf4j
public class IoUtil {

    private final static int MSG_LENGTH_LENGTH = 8;

    private static final int MSG_LENGTH_RADIX = 32;


    private static final ExecutorService IO_POOL = ThreadUtil.createQyFixedThreadPool(50, "IO", null);


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


    /**
     * description: 读取InputStream中的数据直至读到一定长度的    byte
     *
     * @author yqingyu
     * DATE 2022/4/22
     */
    public static byte[] readBytes(InputStream in, int len) throws IORuntimeException {
        if (null == in) {
            return null;
        }
        if (len <= 0) {
            return new byte[0];
        }

        byte[] b = new byte[len];
        int readLength;
        try {
            readLength = in.read(b);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        if (readLength > 0 && readLength < len) {
            byte[] b2 = new byte[readLength];
            System.arraycopy(b, 0, b2, 0, readLength);
            return b2;
        } else if (readLength == -1) {
            return new byte[0];
        } else {
            return b;
        }
    }


    /**
     * description: 读取InputStream中的数据直至读到一定长度的    byte
     *
     * @author yqingyu
     * DATE 2022/4/22
     */
    public static byte[] readBytes(InputStream in, int len, int timeout) throws IORuntimeException {
        if (null == in) {
            return null;
        }
        if (len <= 0) {
            return new byte[0];
        }

        byte[] b = new byte[len];
        int readLength;
        var ref = new Object() {
            int i = 0;
        };


        FutureTask<Integer> task = new FutureTask<>(() -> {
            for (; ref.i < len; ref.i++) {
                int c = in.read();
                if (c == -1) {
                    //保持此位读取 直至读完
                    ref.i -= 1;
                    continue;
                }
                b[ref.i] = (byte) c;
            }
            return ref.i;
        });


        try {
            IO_POOL.execute(task);
            ref.i = task.get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        readLength = ref.i + 1;

        if (readLength > 0 && readLength < len) {
            byte[] b2 = new byte[readLength];
            System.arraycopy(b, 0, b2, 0, readLength);
            return b2;
        } else if (readLength == -1) {
            return new byte[0];
        } else {
            return b;
        }
    }


    /**
     * description:读取SocketChannel中的数据直至读到一定长度的    byte
     *
     * @author yqingyu
     * DATE 2022/4/21
     */
    public static byte[] readBytes(SocketChannel socketChannel, int len) throws IOException {

        ByteBuffer byteBuffer = ByteBuffer.allocate(len);
        byte[] bytes = new byte[0];
        while (true) {
            byteBuffer.clear();
            int n = socketChannel.read(byteBuffer);
            if (n == -1) {
                break;
            }

            byteBuffer.flip();
            int limit = byteBuffer.limit();
            byte[] currentData = new byte[limit];
            for (int i = 0; i < limit; i++) {
                currentData[i] = byteBuffer.get(i);
            }
            bytes = ArrayUtils.addAll(bytes, currentData);

            if (bytes.length == len) {
                break;
            }
        }
        return bytes;

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
    public static void writeMessage(SocketChannel socketChannel, QyMsgHeader msg) throws Exception {
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
    public static void writeMessage(Socket socket, QyMsgHeader msg) throws Exception {


        byte[] bytes = JSON.toJSONBytes(msg);

        byte[] qyMsgBytes = getQyMsgBytes(
                bytes
        );
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(qyMsgBytes);
        outputStream.flush();
    }


    public static QyMsgHeader readMsg(Socket socket) throws Exception {
        byte[] bytes = readQyBytes(socket);
        return JSON.parseObject(bytes, QyMsgHeader.class);
    }

    public static QyMsgHeader readMsg(Socket socket,int timeout) throws Exception {
        byte[] bytes = readQyBytes(socket,timeout);
        return JSON.parseObject(bytes, QyMsgHeader.class);
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


    public static QyMsgHeader readMessage2(SocketChannel socketChannel) throws IOException {

        QyMsgHeader qyMsgHeader = JSON.parseObject(readQyBytes(socketChannel), QyMsgHeader.class);
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
        String len = new String(readBytes(socketChannel, MSG_LENGTH_LENGTH), StandardCharsets.UTF_8);

        return readBytes(socketChannel, Integer.parseInt(len, MSG_LENGTH_RADIX));
    }


    public static String[] readUserMessage(Socket socket) throws IOException {
        String userId = readMessage(socket);
        String msg = readMessage(socket);
        return new String[]{userId, msg};
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
     * socket 转发
     *
     * @param socket1
     * @param socket2
     * @param pool
     * @throws IOException
     * @author YYJ
     * @version 1.0.0
     * @description
     */
    public static void transSocket(Socket socket1, Socket socket2, ThreadPoolExecutor pool, int bufferSize) throws IOException {
        AtomicReference<Boolean> run = new AtomicReference<>();
        AtomicReference<InputStream> is1 = new AtomicReference<>();
        AtomicReference<OutputStream> os1 = new AtomicReference<>();
        AtomicReference<InputStream> is2 = new AtomicReference<>();
        AtomicReference<OutputStream> os2 = new AtomicReference<>();
        run.set(true);


        try {
            if (socket1 != null && socket1.isConnected()) {
                try {
                    is1.set(socket1.getInputStream());
                    os1.set(socket1.getOutputStream());
                } catch (IOException e) {
                    log.error("转发流异常1", e);
                    throw new RuntimeException(e);
                }
            } else {
                return;
            }


            if (socket2.isConnected()) {
                try {
                    is2.set(socket2.getInputStream());
                    os2.set(socket2.getOutputStream());
                } catch (IOException e) {
                    log.error("转发流异常2", e);
                    throw new RuntimeException(e);
                }
            } else {
                return;
            }

            pool.execute(() -> {
                log.info("转发开始1");
                while (run.get()) {
                    try {
                        os2.get().write(IoUtil.readBytes(is1.get(), bufferSize));
                        os2.get().flush();
                    } catch (Exception e) {
                        run.set(false);
                        log.error("转发停止1", e);
                        return;
                    }
                }
                log.info("转发停止1");
            });

            pool.execute(() -> {
                log.info("转发开始 2");
                while (run.get()) {
                    try {
                        os1.get().write(IoUtil.readBytes(is2.get(), 1024));
                        os1.get().flush();
                    } catch (Exception e) {
                        run.set(false);
                        log.error("转发停止2", e);
                        return;
                    }
                }
                log.info("转发停止 2");
            });

        } catch (Exception e) {
            socket2.close();
            socket1.close();
            log.error("forward 异常", e);
        }
    }


}
