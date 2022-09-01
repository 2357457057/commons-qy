package top.yqingyu.common.utils;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;
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
@Slf4j
public class IoUtil {


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


    public static <T> T deserializationObj(byte[] bytes2, Class<T> tClass) throws IOException, ClassNotFoundException {
        byte[] bytes = bytes2;
        ArrayList<Integer> integers = new ArrayList<>();
        for (byte b : bytes) {
            integers.add(Integer.decode("" + b));
        }
        ObjectInputStream inputStream = new ObjectInputStream(new WriteStreamToInputStream(integers));
        T o = (T) inputStream.readObject();
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

    public static final String dict = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            long l = System.currentTimeMillis();
            String random = RandomStringUtils.random(16, dict);
            long l1 = System.currentTimeMillis() - l;
            System.out.println(l1);
            System.out.println(random);

        }
    }
}
