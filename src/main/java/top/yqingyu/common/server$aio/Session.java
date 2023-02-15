package top.yqingyu.common.server$aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.server$aio.Session
 * @description
 * @createTime 2023年02月10日 17:09:00
 */
public abstract class Session {

    protected SessionBridge sessionBridge;

    public Session() {
    }


    public void ready() {


    }


    public void doLogicError(Throwable exec) {
    }

    public final Integer read(ByteBuffer buffer, long timeout, TimeUnit unit) throws InterruptedException {
        sessionBridge.read(buffer, timeout, unit);
        return sessionBridge.readComplete();
    }

    public final Integer write(ByteBuffer buffer, long timeout, TimeUnit unit) throws InterruptedException {
        sessionBridge.write(buffer, timeout, unit);
        return sessionBridge.writeComplete();
    }


    public AsynchronousSocketChannel getChannel() {
        return sessionBridge.getNetChannel().getAChannel();
    }

    public void close() throws IOException {
        sessionBridge.getNetChannel().close();
    }

    void setSessionBridge(SessionBridge sessionBridge) {
        this.sessionBridge = sessionBridge;
    }

    /**
     * description: for http
     *
     * @author yqingyu
     * DATE 2022/4/21
     */
    public byte[] readBytes2(int len) throws InterruptedException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(len);
        byteBuffer.clear();
        int n = read(byteBuffer, 30, TimeUnit.SECONDS);
        byteBuffer.flip();
        int limit = byteBuffer.limit();
        byte[] currentData = new byte[limit];
        byteBuffer.get(currentData);
        return currentData;
    }

    public Integer writeBytes(byte[] bytes, long timeout) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        return write(byteBuffer, timeout, TimeUnit.MILLISECONDS);

    }

    public Integer writeBytes(ByteBuffer byteBuffer, long timeout) throws Exception {
        byteBuffer.flip();
        return write(byteBuffer, timeout, TimeUnit.MILLISECONDS);
    }

    public void writeFile(FileChannel fileChannel, ByteBuffer byteBuffer) throws Exception {
        long size = fileChannel.size();
        long l = 0;
        do {
            byteBuffer.clear();
            fileChannel.read(byteBuffer, l);
            l += blockWrite(byteBuffer);
            byteBuffer.flip();
        } while (l != size);
    }

    public int blockWrite(ByteBuffer buffer) throws Exception {
        return getChannel().write(buffer).get();
    }

}
