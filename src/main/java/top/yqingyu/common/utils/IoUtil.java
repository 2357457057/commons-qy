package top.yqingyu.common.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.bean.NetChannel;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
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
    public static final int spinsTimesMax = 1000;

    /**
     * description: 读取InputStream中的数据读到一定长度的 byte (非绝对长度)
     * (绝对长度)
     *
     * @author yqingyu
     * DATE 2022/4/22
     */
    public static byte[] readBytes(InputStream in, int len) throws IOException {
        if (null == in) {
            return null;
        }
        if (len <= 0) {
            return new byte[0];
        }

        byte[] b = new byte[len];
        int readLength;
        readLength = in.read(b);
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
     * description: 读取InputStream中的数据直至读到一定长度的 byte
     * (绝对长度)
     * 或抛出异常
     *
     * @author yqingyu
     * DATE 2022/4/22
     */
    public static byte[] readBytes2(InputStream in, int len) throws IOException {
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

        for (; ref.i < len; ref.i++) {
            int c = in.read();
            if (c == -1) {
                //保持此位读取 直至读完
                ref.i -= 1;
                continue;
            }
            b[ref.i] = (byte) c;
        }

        readLength = ref.i + 1;

        if (readLength > 0 && readLength < len) {
            byte[] b2 = new byte[readLength];
            System.arraycopy(b, 0, b2, 0, readLength);
            return b2;
        } else {
            return b;
        }
    }

    /**
     * @author YYJ
     * @description 当远端主动断开后 客户端在读取数据时将一直为-1 并不会抛出异常，只有在write的时候才会抛出异常
     */
    public static byte[] readBytes2(InputStream in, int len, AtomicBoolean breakFlag) throws IOException {
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

        for (; ref.i < len; ref.i++) {
            int c = in.read();
            if (c == -1) {
                //保持此位读取 直至读完
                ref.i -= 1;
                if (!breakFlag.get()) break; //跳出
                continue;
            }
            b[ref.i] = (byte) c;
        }

        readLength = ref.i + 1;

        if (readLength > 0 && readLength < len) {
            byte[] b2 = new byte[readLength];
            System.arraycopy(b, 0, b2, 0, readLength);
            return b2;
        } else {
            return b;
        }
    }


    /**
     * 当远端主动断开后 客户端在读取数据时将一直为-1,并不会抛出异常，
     * socket 的状态是本地的，远端断开链接,状态并不会主动感知。
     * 所以需要去写出数据判断是否畅通，
     * 只有在write的时候才会抛出异常，
     * 通过outStream写出数据会影响正常业务数据，
     * {@link java.net.Socket}提供了 {@link java.net.Socket#sendUrgentData} 方法。
     * 不会影响正常的业务数据(远端未开启 {@link java.net.SocketOptions#SO_OOBINLINE} (默认 false)。)
     *
     * @version v3
     * @author YYJ
     */
    public static byte[] readBytes3(Socket socket, int len, AtomicBoolean breakFlag) throws IOException {
        InputStream in = socket.getInputStream();
        if (len <= 0) {
            return new byte[0];
        }

        byte[] b = new byte[len];
        int readLength;
        var ref = new Object() {
            int i = 0;
        };
        int spinsTimes = 0;
        for (; ref.i < len; ref.i++) {
            int c = in.read();
            if (c == -1) {
                //保持此位读取 直至读完
                ref.i -= 1;
                if (!breakFlag.get())
                    break;
                //当超过最大自旋值，去校验socket是否健康。
                if (spinsTimes++ > spinsTimesMax && !testSocket(socket)) {
                    break;
                }
                continue;
            } else {
                spinsTimes = 0;
            }
            b[ref.i] = (byte) c;
        }

        readLength = ref.i + 1;

        if (readLength > 0 && readLength < len) {
            byte[] b2 = new byte[readLength];
            System.arraycopy(b, 0, b2, 0, readLength);
            return b2;
        } else {
            return b;
        }
    }

    public static boolean testSocket(Socket socket) {
        try {
            socket.sendUrgentData(0xFF);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * description: 读取InputStream中的数据直至读到一定长度的    byte
     *
     * @author yqingyu
     * DATE 2022/4/22
     */
    public static byte[] readBytes(InputStream in, int len, int timeout) {
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
     * 严格的定长读取
     */
    public static byte[] readBytes(SocketChannel socketChannel, int len) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(len);
        byte[] bytes;
        do {
            socketChannel.read(byteBuffer);
        } while (byteBuffer.position() != len);
        byteBuffer.flip();
        int limit = byteBuffer.limit();
        bytes = new byte[limit];
        byteBuffer.get(bytes, 0, limit);
        return bytes;

    }

    /**
     * 不严格的定长读取
     */
    public static byte[] readByteLax(SocketChannel socketChannel, int len) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(len);
        byte[] bytes;
        do {
            socketChannel.read(byteBuffer);
        } while (byteBuffer.position() != len);
        byteBuffer.flip();
        int limit = byteBuffer.limit();
        bytes = new byte[limit];
        byteBuffer.get(bytes, 0, limit);
        return bytes;

    }

    /**
     * description:读取SocketChannel中的数据直至读到一定长度的    byte
     *
     * @author yqingyu
     * DATE 2022/4/21
     */
    public static byte[] readBytes(AsynchronousSocketChannel socketChannel, int len) throws IOException, ExecutionException, InterruptedException, TimeoutException {

        ByteBuffer byteBuffer = ByteBuffer.allocate(len);
        byte[] bytes = new byte[0];
        while (true) {
            byteBuffer.clear();
            int n = socketChannel.read(byteBuffer).get(1, TimeUnit.MILLISECONDS);
            if (n == -1) {
                break;
            }
            byteBuffer.flip();
            int limit = byteBuffer.limit();
            byte[] currentData = new byte[limit];
            for (int i = 0; i < limit; i++) {
                currentData[i] = byteBuffer.get(i);
            }
            bytes = ArrayUtil.addAll(bytes, currentData);

            if (bytes.length == len) {
                break;
            }
        }
        return bytes;

    }

    public static byte[] readBytes2(NetChannel netChannel, int len) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        if (netChannel.isNioChannel()) {
            return readBytes2(netChannel.getNChannel(), len);
        }
        return readBytes2(netChannel.getAChannel(), len);
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
        byte[] bytes;
        while (true) {
            int n = socketChannel.read(byteBuffer);
            if (n == -1 || byteBuffer.limit() == len) {
                break;
            }
            if (n == 0 && integer.getAndIncrement() == 3) {
                break;
            }
        }
        byteBuffer.flip();
        int limit = byteBuffer.limit();
        bytes = new byte[limit];
        byteBuffer.get(bytes, 0, limit);
        return bytes;

    }

    /**
     * description: for http
     *
     * @author yqingyu
     * DATE 2022/4/21
     */
    public static byte[] readBytes2(AsynchronousSocketChannel socketChannel, int len) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(len);
        byteBuffer.clear();
        int n = socketChannel.read(byteBuffer).get();
        byteBuffer.flip();
        int limit = byteBuffer.limit();
        byte[] currentData = new byte[limit];
        for (int i = 0; i < limit; i++) {
            currentData[i] = byteBuffer.get(i);
        }
        return currentData;
    }

    /***
     *    待实现。
     * @param socketChannel
     * @return
     * @author YYJ
     * @version 1.0.0
     * @description      */
    public static byte[] readUntilTarget(SocketChannel socketChannel) {
        //TODO 待实现
        return new byte[0];
    }

    public static void writeBytes(SocketChannel socketChannel, byte[] bytes) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytes.length);
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
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytes.length);
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

    public static boolean writeBytes(SocketChannel socketChannel, ByteBuffer byteBuffer, long timeout) throws Exception {
        FutureTask<Boolean> futureTask = new FutureTask<>(() -> {
            int length = byteBuffer.limit();
            byteBuffer.flip();
            long l = 0;
            do {
                l += socketChannel.write(byteBuffer);
            } while (l != length);
            return true;
        });
        Thread thread = new Thread(futureTask);
        thread.setDaemon(true);
        thread.start();
        return futureTask.get(timeout, TimeUnit.MILLISECONDS);
    }

    public static <T> boolean writeBytes(AsynchronousSocketChannel socketChannel, byte[] bytes, long timeout, T attachment, CompletionHandler<Integer, ? super T> handler) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        socketChannel.write(byteBuffer, timeout, TimeUnit.MILLISECONDS, attachment, handler);
        return true;
    }

    public static <T> boolean writeBytes(AsynchronousSocketChannel socketChannel, ByteBuffer byteBuffer, long timeout, T attachment, CompletionHandler<Integer, ? super T> handler) throws Exception {
        byteBuffer.flip();
        socketChannel.write(byteBuffer, timeout, TimeUnit.MILLISECONDS, attachment, handler);
        return true;
    }

    public static <T> boolean writeBytes(NetChannel netChannel, ByteBuffer byteBuffer, long timeout, T attachment, CompletionHandler<Integer, ? super T> handler) throws Exception {
        if (netChannel.isAioChannel()) {
            return writeBytes(netChannel.getAChannel(), byteBuffer, timeout, attachment, handler);
        }
        return writeBytes(netChannel.getNChannel(), byteBuffer, timeout);
    }

    public static <T> boolean writeBytes(NetChannel netChannel, byte[] bytes, long timeout, T attachment, CompletionHandler<Integer, ? super T> handler) throws Exception {
        if (netChannel.isAioChannel()) {
            return writeBytes(netChannel.getAChannel(), bytes, timeout, attachment, handler);
        }
        return writeBytes(netChannel.getNChannel(), bytes, timeout);
    }

    public static int leftToRight(ReadableByteChannel leftChannel, AsynchronousSocketChannel rightChannel, ByteBuffer byteBuffer) throws IOException, ExecutionException, InterruptedException {
        byteBuffer.clear();
        int read = leftChannel.read(byteBuffer);
        byteBuffer.flip();
        long l = 0;
        do {
            l += rightChannel.write(byteBuffer).get();
        } while (read != l);

        return read;
    }

    public static void writeFile(FileChannel fileChannel, SocketChannel channel) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 8);
        long size = fileChannel.size();
        long l = 0;
        do {
            byteBuffer.clear();
            fileChannel.read(byteBuffer, l);
            byteBuffer.flip();
            int limit = byteBuffer.limit();
            l += limit;
            do {
                channel.write(byteBuffer);
            } while (limit != byteBuffer.position());

        } while (l != size);
    }

    public static void writeFile(File file, Socket socket) throws Exception {
        byte[] buf = new byte[1024 * 8];
        FileInputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = socket.getOutputStream();
        while (true) {
            int read = inputStream.read(buf);
            if (read == -1) break;
            outputStream.write(buf, 0, read);
        }
        outputStream.flush();
        inputStream.close();
    }


    public static void writeBytes(Socket socket, byte[] bytes) throws Exception {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
    }

    public static <T> T deserializationObj(byte[] bytes2, Class<T> tClass) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes2));
        T o = (T) inputStream.readObject();
        inputStream.close();
        return o;
    }


    public static byte[] objToSerializBytes(Serializable obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(bos);
        outputStream.writeObject(obj);
        outputStream.flush();
        outputStream.close();
        return bos.toByteArray();
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
