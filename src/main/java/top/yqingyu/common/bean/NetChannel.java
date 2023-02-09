package top.yqingyu.common.bean;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.bean.NetChannel
 * @description
 * @createTime 2023年02月09日 23:49:00
 */
public class NetChannel {
    private SocketChannel nioChannel;
    private boolean isNioChannel = false;
    private AsynchronousSocketChannel aioChannel;
    private boolean isAioChannel = false;


    public NetChannel(SocketChannel nioChannel) {
        isNioChannel = true;
        this.nioChannel = nioChannel;
    }

    public NetChannel(AsynchronousSocketChannel aioChannel) {
        isAioChannel = true;
        this.aioChannel = aioChannel;
    }

    public boolean isAioChannel() {
        return isAioChannel;
    }

    public boolean isNioChannel() {
        return isNioChannel;
    }


    public SocketChannel getNChannel() {
        return nioChannel;
    }

    public AsynchronousSocketChannel getAChannel() {
        return aioChannel;
    }


    public void close() throws IOException {
        if (isNioChannel) {
            nioChannel.close();
            return;
        }
        if (isAioChannel) {
            aioChannel.close();
        }
    }

    public NetChannel shutdownInput() throws IOException {
        if (isNioChannel) {
            nioChannel.shutdownInput();
            return this;
        }
        if (isAioChannel) {
            aioChannel.shutdownInput();
        }
        return this;
    }

    public NetChannel shutdownOutput() throws IOException {
        if (isNioChannel) {
            nioChannel.shutdownOutput();
            return this;
        }
        if (isAioChannel) {
            aioChannel.shutdownOutput();
        }
        return this;
    }

    public Socket socket() {
        if (isNioChannel) {
            return nioChannel.socket();
        }
        return null;
    }

    public boolean isConnected() {
        if (isNioChannel) {
            return nioChannel.isConnected();
        }
        return false;
    }


    public boolean isConnectionPending() {
        if (isNioChannel) {
            return nioChannel.isConnectionPending();
        }
        return false;
    }


    public boolean connect(SocketAddress remote) throws IOException {
        if (isNioChannel) {
            return nioChannel.connect(remote);
        }
        if (isAioChannel) {

            try {
                aioChannel.connect(remote).get();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public void register() {

    }

    public boolean finishConnect() throws IOException {
        if (isNioChannel) {
            return nioChannel.finishConnect();
        }
        return false;
    }


    public SocketAddress getRemoteAddress() throws IOException {
        if (isNioChannel) {
            return nioChannel.getRemoteAddress();
        }
        if (isAioChannel) {
            return aioChannel.getRemoteAddress();
        }
        return null;
    }


    public int read(ByteBuffer dst) throws IOException, ExecutionException, InterruptedException {
        if (isNioChannel) {
            return nioChannel.read(dst);
        }
        if (isAioChannel) {
            return aioChannel.read(dst).get();
        }
        return 0;
    }

    public int read(ByteBuffer dst, long time, TimeUnit unit) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        if (isNioChannel) {
            return nioChannel.read(dst);
        }
        if (isAioChannel) {
            return aioChannel.read(dst).get(time, unit);
        }
        return 0;
    }


    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        if (isNioChannel) {
            return nioChannel.read(dsts, offset, length);
        }
        return 0;
    }

    public <A> void read(ByteBuffer dst, long timeout, TimeUnit unit, A attachment, CompletionHandler<Integer, ? super A> handler) {
        if (isAioChannel) {
            aioChannel.read(dst, timeout, unit, attachment, handler);
        }
    }

    public <A> void read(ByteBuffer[] dsts, int offset, int length, long timeout, TimeUnit unit, A attachment, CompletionHandler<Long, ? super A> handler) {
        if (isAioChannel) {
            aioChannel.read(dsts, offset, length, timeout, unit, attachment, handler);
        }
    }

    public int write(ByteBuffer src) throws IOException, ExecutionException, InterruptedException {
        if (isNioChannel) {
            return nioChannel.write(src);
        }
        if (isAioChannel) {
            return aioChannel.write(src).get();
        }
        return 0;
    }

    public int write(ByteBuffer src, long time, TimeUnit unit) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        if (isNioChannel) {
            return nioChannel.write(src);
        }
        if (isAioChannel) {
            return aioChannel.write(src).get(time, unit);
        }
        return 0;
    }


    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        if (isNioChannel) {
            return nioChannel.read(srcs, offset, length);
        }
        return 0;
    }


    public <A> void write(ByteBuffer src, long timeout, TimeUnit unit, A attachment, CompletionHandler<Integer, ? super A> handler) {
        if (isAioChannel) {
            aioChannel.write(src, timeout, unit, attachment, handler);
        }
    }


    public <A> void write(ByteBuffer[] srcs, int offset, int length, long timeout, TimeUnit unit, A attachment, CompletionHandler<Long, ? super A> handler) {
        if (isAioChannel) {
            aioChannel.write(srcs, offset, length, timeout, unit, attachment, handler);
        }
    }


    public <A> void connect(SocketAddress remote, A attachment, CompletionHandler<Void, ? super A> handler) {
        if (isAioChannel) {
            aioChannel.connect(remote, attachment, handler);
        }
    }

    public <T> AsynchronousSocketChannel setOption(SocketOption<T> name, T value) throws IOException {
        if (isAioChannel) {
            aioChannel.setOption(name, value);
        }
        return null;
    }

    public <T> T getOption(SocketOption<T> name) throws IOException {
        if (isAioChannel) {
            return aioChannel.getOption(name);
        }
        return null;
    }

    public SocketAddress getLocalAddress() throws IOException {
        if (isNioChannel) {
            return nioChannel.getLocalAddress();
        }
        if (isAioChannel) {
            return aioChannel.getLocalAddress();
        }
        return null;
    }

    public SelectionKey register(Selector sel, int ops) throws ClosedChannelException {
        if (isNioChannel) {
            return nioChannel.register(sel, ops);
        }
        return null;
    }

    public boolean isOpen() {
        if (isNioChannel) {
            return nioChannel.isOpen();
        }
        if (isAioChannel) {
            return aioChannel.isOpen();
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (isNioChannel) {
            return nioChannel.hashCode();
        }
        if (isAioChannel) {
            return aioChannel.hashCode();
        }
        return super.hashCode();
    }
}
