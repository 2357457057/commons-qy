package top.yqingyu.common.nio$server.event$http.compoment;

import cn.hutool.core.date.LocalDateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.utils.ArrayUtil;
import top.yqingyu.common.utils.IoUtil;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static top.yqingyu.common.nio$server.event$http.compoment.Response.*;
import static top.yqingyu.common.utils.ArrayUtil.*;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$http.route.DoRequest
 * @description
 * @createTime 2022年09月14日 18:34:00
 */

class DoRequest implements Callable<Object>{

    private final LinkedBlockingQueue<Object> QUEUE;

    private final SocketChannel socketChannel;
    static long DEFAULT_BUF_LENGTH;
    //最大Body长度 64M
    static long MAX_BODY_SIZE;

    //最大header长度 128KB
    static long MAX_HEADER_SIZE;


    private static final Logger log = LoggerFactory.getLogger(DoRequest.class);

    DoRequest(SocketChannel socketChannel, LinkedBlockingQueue<Object> QUEUE) {
        this.socketChannel = socketChannel;
        this.QUEUE = QUEUE;
    }

    @Override
    public Object call() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        try {
            HttpAction httpAction = parseRequest();

            Request request = null;
            Response response = null;

            if (httpAction instanceof Request) {
                request = (Request) httpAction;
                //未找到本地资源
            } else if (httpAction instanceof Response)
                response = (Response) httpAction;

            //进行response
            createResponse(request, response, false);
        } finally {
            //处理完毕需要需丢掉SINGLE中的记录
            int i = socketChannel.hashCode();
            log.info("{}出 cost {} MICROS", i, LocalDateTimeUtil.between(now, LocalDateTime.now(), ChronoUnit.MICROS));
        }
        return null;
    }


    private void createResponse(Request request, Response response, boolean notEnd) throws InterruptedException {
        HttpEventEntity httpEventEntity = new HttpEventEntity(socketChannel, notEnd);
        httpEventEntity.setRequest(request);
        httpEventEntity.setResponse(response);
        QUEUE.put(httpEventEntity);
    }

    private HttpAction parseRequest() throws Exception {
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
                temp = IoUtil.readBytes2(socketChannel, (int) DEFAULT_BUF_LENGTH);
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
                request.setParseEnd();
                break;
                //当报文总长度超出 DEFAULT_BUF_LENGTH
            } else {
                all = ArrayUtil.addAll(all, temp);

                //头部解析
                if (!flag) {

                    //header超出最大值直接关闭连接
                    if (all.length > MAX_HEADER_SIZE) {
                        return $400_BAD_REQUEST.putHeaderDate(ZonedDateTime.now()).setAssemble(true);
                    }

                    ArrayList<byte[]> bytes = splitByTarget(all, RN_RN);
                    //当且仅当找到了/r/n/r/n
                    if (bytes.size() != 0) {
                        // 头部已解析
                        flag = true;
                        assembleHeader(request, bytes.get(0));
                        // 当只收到消息头，且消息头有 Content-Length 且Content-Length在一定的范围内 此时需要
                        if (bytes.size() == 1 && StringUtils.equalsIgnoreCase("0", request.getHeader().getString("Content-Length"))) {
                            Response response = $100_CONTINUE.putHeaderDate(ZonedDateTime.now());
                            createResponse(request, response, true);
                        }
                    }
                }

                //get无body
                if (HttpMethod.GET.equals(request.getMethod())) {
                    request.setParseEnd();
                    return request;
                }

                //body解析 当head已解析
                if (flag) {
                    long contentLength = request.getHeader().getLongValue("Content-Length", -1);

                    //body超出最大值直接关闭连接
                    if (contentLength > MAX_BODY_SIZE || all.length > MAX_BODY_SIZE) {
                        return $413_ENTITY_LARGE.putHeaderDate(ZonedDateTime.now()).setAssemble(true);
                    }

                    //说明已经读完
                    if (currentLength < DEFAULT_BUF_LENGTH || all.length == currentLength) {
                        int idx = firstIndexOfTarget(all, RN_RN);
                        int efIdx = idx + RN_RN.length;
                        //发生这个很奇怪。
                        if (idx == -1) {
                            return $400_BAD_REQUEST.putHeaderDate(ZonedDateTime.now()).setAssemble(true);
                            //最后一位
                        } else if (efIdx == all.length) {
                            request.setParseEnd();
                        } else {
                            byte[] body = new byte[all.length - efIdx];

                            //去除多余的数据
                            if (contentLength != -1) {
                                body = new byte[(int) contentLength];
                            }
                            System.arraycopy(all, efIdx, body, 0, body.length);
                            request.setBody(body);
                            request.setParseEnd();
                        }

                    }
                }
            }
        } while (!request.isParseEnd() && all.length != 0);
        return request;
    }

    static void assembleHeader(Request request, byte[] header) {
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
            request.putHeader(headerName_value.get(0), headerName_value.size() == 2 ? headerName_value.get(1) : null);
        }
    }


}