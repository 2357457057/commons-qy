package top.yqingyu.common.nio$server.event$http.compoment;

import cn.hutool.core.date.LocalDateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.qydata.ConcurrentDataMap;
import top.yqingyu.common.utils.GzipUtil;
import top.yqingyu.common.utils.IoUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$http.compoment.DoResponse
 * @description
 * @createTime 2022年09月19日 15:19:00
 */
class DoResponse implements Callable<Object> {

    private final LinkedBlockingQueue<Object> QUEUE;
    private final Selector selector;


    static long DEFAULT_SEND_BUF_LENGTH;

    static boolean FILE_COMPRESS_ON;

    //最大压缩源文件大小 128MB
    static long MAX_SINGLE_FILE_COMPRESS_SIZE;

    //是否开启缓存池
    static boolean CACHE_POOL_ON;

    //最大缓存池大小 1.5GB
    static long MAX_FILE_CACHE_SIZE;

    static long SESSION_TIME_OUT;

    private final AtomicLong CurrentFileCacheSize = new AtomicLong();

    private static final ConcurrentDataMap<String, byte[]> FILE_BYTE_CACHE = new ConcurrentDataMap<>();

    private static final Logger log = LoggerFactory.getLogger(DoResponse.class);

    public DoResponse( LinkedBlockingQueue<Object> QUEUE, Selector selector) {
        this.QUEUE = QUEUE;
        this.selector = selector;
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    @SuppressWarnings("all")
    public Object call() throws Exception {

        HttpEventEntity httpEventEntity;
        SocketChannel socketChannel = null;
        LocalDateTime now = LocalDateTime.now();
        try {
            do {
                httpEventEntity = (HttpEventEntity) QUEUE.take();

                socketChannel = httpEventEntity.getSocketChannel();

                Request request = httpEventEntity.getRequest();
                Response response = httpEventEntity.getResponse();


                if (response == null)
                    response = new Response();
                if (request == null) {
                    request = new Request();
                }


                AtomicReference<Response> resp = new AtomicReference<>();
                resp.set(response);
                response.setHttpVersion(HttpVersion.V_1_1);
                response.putHeaderDate(ZonedDateTime.now());


                //响应初始化，寻找本地资源 已组装完成的消息会跳过
                initResponse(request, resp);

                //压缩
                if (FILE_COMPRESS_ON)
                    compress(request, resp);

                doResponse(resp, socketChannel);
            } while (httpEventEntity.isNotEnd());
            socketChannel.register(selector,SelectionKey.OP_READ);
            log.debug("{} cost {} MICROS", socketChannel.hashCode(), LocalDateTimeUtil.between(now, LocalDateTime.now(), ChronoUnit.MICROS));
        }catch (NullPointerException e){
            socketChannel.shutdownInput();
            socketChannel.shutdownOutput();
            socketChannel.close();
        } catch (Exception e){
            log.error("",e);
            socketChannel.shutdownInput();
            socketChannel.shutdownOutput();
            socketChannel.close();
        }
        return null;
    }


    /**
     * 寻找相应的资源。
     *
     * @author YYJ
     * @description
     */
    void initResponse(Request request, AtomicReference<Response> resp) {

        Response response = resp.get();
        //优先文件资源
        if (!response.isAssemble()) {
            LocationMapping.fileResourceMapping(request, response);
        }

        //接口
        if (!response.isAssemble()) {
            //session相关逻辑
            Session session = null;
            String sessionID = request.getCookie(Session.name);
            if (Session.SESSION_CONTAINER.containsKey(sessionID))
                session = Session.SESSION_CONTAINER.get(sessionID);
            else {
                session = new Session();
                Session.SESSION_CONTAINER.put(session.getSessionVersionID(), session);
            }
            request.setSession(session);

            //接口资源
            LocationMapping.beanResourceMapping(request, response);


            if (response.isAssemble() && request.getSession().isNewInstance()) {
                session.setNewInstance(false);
                Cookie cookie = new Cookie(Session.name, session.getSessionVersionID());
                cookie.setMaxAge((int) SESSION_TIME_OUT);
                response.addCookie(cookie);
            }

        }

        //NotFound
        if (!response.isAssemble()) {
            resp.setRelease(Response.$404_NOT_FOUND.putHeaderDate(ZonedDateTime.now()));
        }

    }


    /**
     * 对response 压缩、缓存。
     *
     * @param resp    响应
     * @param request 请求
     * @author YYJ
     * @description
     */
    private void compress(Request request, AtomicReference<Response> resp) throws IOException {
        Response response = resp.get();
        String url = request.getUrl();
        ContentType requestCtTyp = ContentType.parse(request.getHeader("Content-Type"));

        Charset charset;
        if (requestCtTyp != null)
            charset = requestCtTyp.getCharset() == null ? StandardCharsets.UTF_8 : requestCtTyp.getCharset();
        else charset = StandardCharsets.UTF_8;
        if (!"304|100".contains(response.getStatue_code()) || (response.getStrBody() != null ^ response.gainFileBody() == null)) {
            if (request.canCompress()) {
                String strBody = response.getStrBody();
                if (StringUtils.isNotBlank(strBody)) {
                    byte[] bytes = GzipUtil.$2CompressBytes(strBody, charset);
                    response.setCompress_body(bytes);
                    response.putHeaderContentLength(bytes.length).putHeaderCompress();
                } else {
                    if (FILE_BYTE_CACHE.containsKey(url)) {
                        byte[] bytes = FILE_BYTE_CACHE.get(url);
                        response.setCompress_body(bytes);
                        response.putHeaderContentLength(bytes.length).putHeaderCompress();
                    } else {
                        File file = response.getFile_body();
                        long length = file.length();
                        if (length < MAX_SINGLE_FILE_COMPRESS_SIZE && CurrentFileCacheSize.get() < MAX_FILE_CACHE_SIZE) {
                            byte[] bytes = GzipUtil.$2CompressBytes(response.getFile_body());
                            //开启压缩池
                            if (CACHE_POOL_ON) {
                                FILE_BYTE_CACHE.put(url, bytes);
                                CurrentFileCacheSize.addAndGet(bytes.length);
                            }

                            response.setCompress_body(bytes);
                            response.putHeaderContentLength(bytes.length).putHeaderCompress();
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("all")
    private void doResponse(AtomicReference<Response> resp, SocketChannel socketChannel) throws Exception {


        Response response = resp.get();


        ContentType type = response.gainHeaderContentType();
        byte[] bytes;
        if (type.getCharset() != null)
            bytes = response.toString().getBytes(type.getCharset());
        else
            bytes = response.toString().getBytes();

        //Header
        IoUtil.writeBytes(socketChannel, bytes);
        //body
        if (!"304|100".contains(response.getStatue_code()) || (response.getStrBody() != null ^ response.gainFileBody() == null)) {
            byte[] buf = new byte[(int) DEFAULT_SEND_BUF_LENGTH];
            int length;
            File file_body = response.getFile_body();
            if (file_body != null && !response.isCompress()) {
                FileChannel channel = new FileInputStream(response.getFile_body()).getChannel();
                long l = 0;
                long size = channel.size();
                do {
                    l += channel.transferTo(l, DEFAULT_SEND_BUF_LENGTH, socketChannel);
                } while (l != size);
                channel.close();
            } else {
                IoUtil.writeBytes(socketChannel, response.gainBodyBytes());
            }
        }

        log.debug("Response: {}", response.toJsonString());
    }

}
