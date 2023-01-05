import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.nio$server.core.EventHandler;
import top.yqingyu.common.utils.IoUtil;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;

public class aa extends EventHandler {

    Logger logger = LoggerFactory.getLogger(aa.class);

    public aa(Selector selector) throws IOException {
        super(selector);
    }

    /**
     * 单次加载资源
     *
     * @author YYJ
     * @description
     */
    @Override
    protected void loading() {
        System.out.println("loading");
    }

    /**
     * @param selector
     * @param socketChannel
     * @throws Exception
     */
    @Override
    public void read(Selector selector, SocketChannel socketChannel) throws Exception {
        byte[] bytes = IoUtil.readBytes2(socketChannel, 5);
        String s = new String(bytes, StandardCharsets.UTF_8);
        int p = Integer.parseInt(s, 32);
        FileChannel open = FileChannel.open(test.file.toPath(), StandardOpenOption.WRITE);

        long l =  open.transferFrom(socketChannel, Math.max(p * test.l - 1, 0),test.l);
        //        byte[] bytes2 = IoUtil.readBytes(socketChannel, 4096);
//        ByteBuffer allocate = ByteBuffer.allocate(bytes2.length);
//        allocate.put(bytes2);
//        allocate.flip();
//        long l = open.write(allocate,);
        logger.info("head:{} body:{}", p, l);
        open.close();
        socketChannel.close();
    }

    /**
     * @param selector
     * @param socketChannel
     * @throws Exception
     */
    @Override
    public void write(Selector selector, SocketChannel socketChannel) throws Exception {

    }

    /**
     * @param selector
     * @param socketChannel
     * @throws Exception
     */
    @Override
    public void assess(Selector selector, SocketChannel socketChannel) throws Exception {

    }
}