package top.yqingyu.common.nio$server.event$http.event;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.nio$server.core.EventHandler;
import top.yqingyu.common.nio$server.event$http.compoment.LocationMapping;
import top.yqingyu.common.nio$server.event$http.entity.HttpVersion;
import top.yqingyu.common.nio$server.event$http.entity.Request;
import top.yqingyu.common.nio$server.event$http.entity.Response;
import top.yqingyu.common.utils.ArrayUtil;
import top.yqingyu.common.utils.IoUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import static top.yqingyu.common.utils.ArrayUtil.*;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.event$handler.HttpEventHandler
 * @description
 * @createTime 2022年09月09日 18:05:00
 */

public class HttpEventHandler extends EventHandler {


    public HttpEventHandler(Selector selector, ThreadPoolExecutor pool) {
        super(selector, pool);
    }

    private static final int DEFAULT_BUF_LENGTH = 1024;
    private static final Logger log = LoggerFactory.getLogger(HttpEventHandler.class);

    static {

    }
    @Override
    public void read(Selector selector, SelectionKey selectionKey) throws Exception {


        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        int currentLength;
        byte[] all = new byte[0];
        AtomicInteger enumerator = new AtomicInteger();
        Request request = new Request();
        do {

            int currentStep = enumerator.getAndIncrement();

            byte[] temp = new byte[0];
            try {
                temp = IoUtil.readBytes2(socketChannel, DEFAULT_BUF_LENGTH);
            } catch (IOException e) {
                socketChannel.close();
            }
            currentLength = temp.length;

            if (currentStep == 0 && temp.length < 1024 && currentLength != 0) {

                ArrayList<byte[]> Info$header$body = ArrayUtil.splitByTarget(temp, RN_RN); //只剩body
                ArrayList<byte[]> info$header = ArrayUtil.splitByTarget(Info$header$body.remove(0), RN); //只剩header
                ArrayList<byte[]> info = splitByTarget(info$header.remove(0), SPACE);

                request.setMethod(info.get(0));
                request.setUrl(info.get(1));
                request.setHttpVersion(info.get(2));

                for (byte[] bytes : info$header) {
                    ArrayList<byte[]> headerName_value = splitByTarget(bytes, COLON_SPACE);
                    request.putHeader(headerName_value.get(0), headerName_value.get(1));
                }

                byte[] body = EMPTY_BYTE_ARRAY;

                for (byte[] bytes : Info$header$body) {
                    body = ArrayUtil.addAll(body, bytes);
                }

                request.setBody(body);

                log.info("RCV_Request location: {}", request);


                Response response = new Response();
                response.setHttpVersion(HttpVersion.V_1_1);
                response.putHeaderDate(LocalDateTime.now(ZoneId.systemDefault()));

                File file = LocationMapping.initResourceHeader(request.getUrl(), response, request);
                //响应头
                IoUtil.writeBytes(socketChannel, response.toString().getBytes(StandardCharsets.UTF_8));

                if (file != null && "200".equals(response.getState_code())) {
                    FileInputStream resourceStream = new FileInputStream(file);
                    byte[] buf = new byte[1024];
                    int length;
                    while ((length = resourceStream.read(buf, 0, 1024)) > 0) {
                        byte[] temps = new byte[length];
                        System.arraycopy(buf, 0, temps, 0, length);
                        buf = temps;
                        IoUtil.writeBytes(socketChannel, buf);
                        buf = new byte[1024];
                    }
                    resourceStream.close();
                }
                log.info(response.toString());
//                socketChannel.register(selector, SelectionKey.OP_READ);
                socketChannel.close();
                break;
            } else if (currentLength == 0) {

                try {
                    IoUtil.writeBytes(socketChannel, "HTTP/1.1 200 OK".getBytes(StandardCharsets.UTF_8));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            } else {
                if (currentLength == DEFAULT_BUF_LENGTH) {
                    all = ArrayUtil.addAll(all, temp);
                } else {

                }
            }


        } while (currentLength == DEFAULT_BUF_LENGTH);


//        socketChannel.register(selector, SelectionKey.OP_WRITE);
    }


    @Override
    public void write(Selector selector, SelectionKey selectionKey) throws Exception {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        IoUtil.writeBytes(socketChannel, ("HTTP/1.1 200 OK\r\n" +
                "Content-Type:application/json\r\n" +
                "\r\n" +
                "{}").getBytes(StandardCharsets.UTF_8));
        socketChannel.register(selector, SelectionKey.OP_READ);

        socketChannel.close();
    }


    @Override
    public void assess(Selector selector, SelectionKey selectionKey) throws Exception {

        while (true) {
            try {
                Object take = QUEUE.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                write(selector, selectionKey);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }

    }


}
