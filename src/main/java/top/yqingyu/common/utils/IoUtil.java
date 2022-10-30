package top.yqingyu.common.utils;

import cn.hutool.core.io.IORuntimeException;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author YYJ
 * @version 1.0.0
 * @date 2022/4/21 3:43
 * @description
 * @modified by
 */

public class IoUtil {

    private static final Logger log = LoggerFactory.getLogger(IoUtil.class);

    /**
     * description: 读取InputStream中的数据读到一定长度的 byte
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
            new Thread(task).start();
            ref.i = task.get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
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

    /**
     * description: for http
     *
     * @author yqingyu
     * DATE 2022/4/21
     */
    public static byte[] readBytes2(SocketChannel socketChannel, int len) throws IOException {

        AtomicInteger integer = new AtomicInteger();
        ByteBuffer byteBuffer = ByteBuffer.allocate(len);
        byte[] bytes = new byte[0];
        while (true) {
            byteBuffer.clear();
            int n = socketChannel.read(byteBuffer);
            if (n == -1) {
                break;
            }
            if (n == 0) {
                if (integer.getAndIncrement() == 3) {
                    break;
                }
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

    /***
     *    待实现。
     * @param socketChannel
     * @return
     * @author YYJ
     * @version 1.0.0
     * @description      */
    public static byte[] readUntilTarget(SocketChannel socketChannel){
        //TODO 待实现
        return new byte[0];
    }
    public static void writeBytes(SocketChannel socketChannel, byte[] bytes) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        long l = 0;
        do {
            int i = socketChannel.write(byteBuffer);
            l += i;
        } while (l != bytes.length);
    }

    public static boolean writeBytes(SocketChannel socketChannel, byte[] bytes, long timeout) throws Exception {
        FutureTask<Boolean> futureTask = new FutureTask<>(() -> {
            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.flip();
            long l = 0;
            do {
                l += socketChannel.write(byteBuffer);
            } while (l != bytes.length);
            return true;
        });
        Thread thread = new Thread(futureTask);
        thread.setDaemon(true);
        thread.start();
        return futureTask.get(timeout, TimeUnit.MILLISECONDS);
    }

    public static void writeBytes(Socket socket, byte[] bytes) throws Exception {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
    }

    public static <T> T deserializationObj(byte[] bytes2, Class<T> tClass) throws IOException, ClassNotFoundException {
        ArrayList<Integer> integers = new ArrayList<>();
        for (byte b : bytes2) {
            integers.add(Integer.decode("" + b));
        }
        WriteStreamToInputStream stream = new WriteStreamToInputStream(integers);

        ObjectInputStream inputStream = new ObjectInputStream(stream);
        T o = (T) inputStream.readObject();
        stream.close();
        inputStream.close();
        return o;
    }


    public static byte[] objToSerializBytes(Serializable obj) throws IOException {
        LinkedList<Integer> integers = new LinkedList<>();
        ObjectOutputStream outputStream = new ObjectOutputStream(new ReadStreamFromOutputStream(integers));
        outputStream.writeObject(obj);
        outputStream.flush();
        outputStream.close();
        byte[] bytes = new byte[integers.size()];
        AtomicInteger atomicInteger = new AtomicInteger();
        integers.forEach(integer -> {
            byte a = 0;
            bytes[atomicInteger.getAndIncrement()] = integer.byteValue();
        });
        return bytes;
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

    /**
     * 从OutputStream 读取数据
     *
     * @author YYJ
     * @version 1.0.0
     * @description
     */
    public static class ReadStreamFromOutputStream extends OutputStream {
        private final Queue<Integer> list;


        public ReadStreamFromOutputStream(Queue<Integer> list) {
            this.list = list;
        }

        @Override
        public void write(int b) throws IOException {
            if (b != -1) {
                list.add(b);
            }
        }

        public void clean() {
            list.clear();
        }
    }

    /**
     * @author YYJ
     * @version 1.0.0
     * @description 将流写入InputStream
     */
    public static class WriteStreamToInputStream extends InputStream {

        private final List<Integer> list;
        private final AtomicInteger atomicInteger = new AtomicInteger();

        public WriteStreamToInputStream(List<Integer> list) {
            this.list = list;
        }

        @Override
        public int read() throws IOException {
            int i = atomicInteger.getAndIncrement();
            if (i < list.size())
                return list.get(i);
            else
                return -1;
        }

        public void clean() {
            list.clear();
            atomicInteger.set(0);
        }
    }

    /**
     * @author YYJ
     * @version 1.0.0
     * @description 将流写入InputStream
     */
    public static class WriteStreamToInputStream2 extends InputStream {

        private final byte[] bytes;
        private final AtomicInteger atomicInteger = new AtomicInteger(0);

        public WriteStreamToInputStream2(byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public int read() {
            int i = atomicInteger.getAndIncrement();
            int ret;
            if (i < bytes.length) {
                ret = bytes[i];

                return ret;
            } else
                return -1;
        }
    }

}
