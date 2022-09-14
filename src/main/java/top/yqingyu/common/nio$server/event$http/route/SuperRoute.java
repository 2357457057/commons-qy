package top.yqingyu.common.nio$server.event$http.route;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.nio$server.event$http.compoment.LocationMapping;
import top.yqingyu.common.nio$server.event$http.entity.ContentType;
import top.yqingyu.common.nio$server.event$http.entity.HttpVersion;
import top.yqingyu.common.nio$server.event$http.entity.Request;
import top.yqingyu.common.nio$server.event$http.entity.Response;
import top.yqingyu.common.utils.ArrayUtil;
import top.yqingyu.common.utils.IoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static top.yqingyu.common.nio$server.event$http.entity.Response.*;
import static top.yqingyu.common.utils.ArrayUtil.*;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$http.route.SuperRoute
 * @description
 * @createTime 2022年09月14日 18:34:00
 */

public class SuperRoute {


    private static final int DEFAULT_BUF_LENGTH = 1024;
    //最大Body长度 64M
    private static final long MaxBodyLength = 1024 * 1024 * 64;
    ;
    //最大header长度 128KB
    private static final long MaxHeaderLength = 1024 * 128;

    private static final Logger log = LoggerFactory.getLogger(SuperRoute.class);

    public void superServlet(SocketChannel socketChannel) throws Exception {
        Request request = parseRequest(socketChannel);

        Response response = new Response();
        response.setHttpVersion(HttpVersion.V_1_1);
        response.putHeaderDate(ZonedDateTime.now());
        AtomicReference<Response> resp = new AtomicReference<>();

        resp.set(response);

        initResponse(request, resp);
        //响应
        doResponse(socketChannel, resp);


    }

    public static void initResponse(Request request, AtomicReference<Response> resp) {

        Response response = resp.get();
        //优先文件资源
        if (!response.isAssemble())
            LocationMapping.fileResourceMapping(request, response);
        //接口
        if (!response.isAssemble())
            LocationMapping.beanResourceMapping(request, response);

        //NotFount
        if (!response.isAssemble()) {
            resp.setRelease(Response.$404_NOT_FOUND.putHeaderDate(ZonedDateTime.now()));
        }

    }

    private void doResponse(SocketChannel socketChannel, AtomicReference<Response> resp) throws Exception {
        Response response = resp.get();
        ContentType type = response.gainHeaderContentType();
        //Header
        IoUtil.writeBytes(socketChannel, response.toString().getBytes(type.getCharset()));

        //body
        if (!"304".equals(response.getStatue_code()) || (response.getStrBody() != null ^ response.gainFileBody() == null)) {
            InputStream resourceStream = response.gainBodyStream();
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

        log.debug("Response: {}", response.toJsonString());
    }


    private Request parseRequest(SocketChannel socketChannel) throws Exception {
        int currentLength;
        byte[] all = new byte[0];
        AtomicInteger enumerator = new AtomicInteger();
        Request request = new Request();
        // 头部是否已解析
        boolean flag = false;
        do {
            int currentStep = enumerator.getAndIncrement();
            byte[] temp = new byte[0];
            try {
                temp = IoUtil.readBytes2(socketChannel, DEFAULT_BUF_LENGTH);
            } catch (IOException e) {
                socketChannel.close();
            }

            currentLength = temp.length;
            //当报文总长度不足 DEFAULT_BUF_LENGTH

            if (currentStep == 0 && temp.length < DEFAULT_BUF_LENGTH && currentLength != 0) {
                ArrayList<byte[]> Info$header$body = ArrayUtil.splitByTarget(temp, RN_RN);

                assembleHeader(request, Info$header$body.remove(0));

                byte[] body = EMPTY_BYTE_ARRAY;
                for (byte[] bytes : Info$header$body) {
                    body = ArrayUtil.addAll(body, bytes);
                }
                request.setBody(body);
                request.setParseEnd(true);
                break;
                //当报文总长度超出 DEFAULT_BUF_LENGTH
            } else {
                all = ArrayUtil.addAll(all, temp);

                //头部解析
                if (!flag) {

                    //body超出最大值直接关闭连接
                    if (all.length > MaxHeaderLength) {
                        Response response = $400_BAD_REQUEST.putHeaderDate(ZonedDateTime.now());
                        IoUtil.writeBytes(socketChannel, request.toString().getBytes(StandardCharsets.UTF_8));
                        log.error(response.toJsonString());
                        socketChannel.close();
                        break;
                    }

                    ArrayList<byte[]> bytes = splitByTarget(all, RN_RN);
                    //当且仅当找到了/r/n/r/n
                    if (bytes.size() != 0) {
                        // 头部已解析
                        flag = true;
                        assembleHeader(request, bytes.get(0));
                        // 当只收到消息头，且消息头有 Content-Length 且Content-Length在一定的范围内
                        if (bytes.size() == 1 && StringUtils.equalsIgnoreCase("0", request.getHeader().getString("Content-Length"))) {

                            Response response = $100_CONTINUE.putHeaderDate(ZonedDateTime.now());
                            IoUtil.writeBytes(socketChannel, response.toString().getBytes(StandardCharsets.UTF_8));
                            log.debug(response.toJsonString());
                        }
                    }
                }

                //body解析
                if (flag) {
                    long contentLength = request.getHeader().getLongValue("Content-Length", -1);

                    //body超出最大值直接关闭连接
                    if (contentLength > MaxBodyLength || all.length > MaxBodyLength) {

                        Response response = $413_ENTITY_LARGE.putHeaderDate(ZonedDateTime.now());
                        IoUtil.writeBytes(socketChannel, request.toString().getBytes(StandardCharsets.UTF_8));
                        log.error(response.toJsonString());
                        socketChannel.close();
                        break;
                    }

                    //说明已经读完
                    if (currentLength < DEFAULT_BUF_LENGTH) {
                        int idx = firstIndexOfTarget(all, RN_RN);
                        int efIdx = idx + RN_RN.length;
                        if (idx == -1) {
                            Response response = $400_BAD_REQUEST.putHeaderDate(ZonedDateTime.now());
                            IoUtil.writeBytes(socketChannel, request.toString().getBytes(StandardCharsets.UTF_8));
                            log.error(response.toJsonString());
                            socketChannel.close();
                            break;
                            //最后一位
                        } else if (efIdx == all.length) {
                            request.setParseEnd(true);
                        } else {
                            byte[] body = new byte[all.length - efIdx];

                            //去除多余的数据
                            if (contentLength != -1) {
                                body = new byte[(int) contentLength];
                            }
                            System.arraycopy(all, efIdx, body, 0, body.length);
                            request.setBody(body);
                            request.setParseEnd(true);
                        }

                    }
                }
            }

        } while (!request.isParseEnd());

        log.debug("Request: {}", request);
        return request;
    }

    public static void assembleHeader(Request request, byte[] header) {
        //只剩body
        ArrayList<byte[]> info$header = ArrayUtil.splitByTarget(header, RN);
        ArrayList<byte[]> info = splitByTarget(info$header.remove(0), SPACE);

        request.setMethod(info.get(0));
        request.setUrl(info.get(1));
        request.setHttpVersion(info.get(2));

        int i = StringUtils.indexOf(request.getUrl(), '?');

        if (i != -1) {
            String substring = request.getUrl().substring(i + 1);
            String[] split = substring.split("&");
            for (int j = 0; j < split.length; j++) {

                String[] urlParamKV = split[j].split("=");
                if (urlParamKV.length == 2)
                    request.putUrlParam(urlParamKV[0], urlParamKV[1]);
                else
                    request.putUrlParam("NoKey_" + j, split[j]);
            }
        }
        for (byte[] bytes : info$header) {
            ArrayList<byte[]> headerName_value = splitByTarget(bytes, COLON_SPACE);
            request.putHeader(headerName_value.get(0), headerName_value.get(1));
        }
    }


}
