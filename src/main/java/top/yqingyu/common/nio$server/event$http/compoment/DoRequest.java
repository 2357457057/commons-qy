package top.yqingyu.common.nio$server.event$http.compoment;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.nio$server.core.RebuildSelectorException;
import top.yqingyu.common.utils.ArrayUtil;
import top.yqingyu.common.utils.IoUtil;
import top.yqingyu.common.utils.StringUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Stack;
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

class DoRequest implements Callable<Object> {

    private final LinkedBlockingQueue<Object> QUEUE;

    private final SocketChannel socketChannel;
    static long DEFAULT_BUF_LENGTH;
    //最大Body长度 64M
    static long MAX_BODY_SIZE;

    //最大header长度 128KB
    static long MAX_HEADER_SIZE;

    static boolean ALLOW_UPDATE = true;

    private static final Logger log = LoggerFactory.getLogger(DoRequest.class);

    DoRequest(SocketChannel socketChannel, LinkedBlockingQueue<Object> QUEUE) {
        this.socketChannel = socketChannel;
        this.QUEUE = QUEUE;
    }

    @Override
    public Object call() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        HttpAction httpAction = null;
        try {
            httpAction = parseRequest();

            Request request = null;
            Response response = null;

            if (httpAction instanceof Request) {
                request = (Request) httpAction;
                //未找到本地资源/一些异常情况,在parse过程中产生响应对象
            } else if (httpAction instanceof Response) {
                response = (Response) httpAction;
            }

            //进行response
            createResponse(request, response, false);
        } catch (RebuildSelectorException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.debug(JSON.toJSONString(httpAction));
            //处理完毕需要需丢掉SINGLE中的记录
            log.debug("{}出 cost {} MICROS", socketChannel.hashCode(), LocalDateTimeUtil.between(now, LocalDateTime.now(), ChronoUnit.MICROS));
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
        InetSocketAddress remoteAddress = (InetSocketAddress) socketChannel.getRemoteAddress();
        request.setInetSocketAddress(remoteAddress);
        request.setHost(remoteAddress.getHostString());
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

                assembleHeader(request, Info$header$body.remove(0), socketChannel);

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
                        assembleHeader(request, bytes.get(0), socketChannel);
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

                    //说明已经读完或读到头了
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
                            int currentContentLength = all.length - efIdx;
                            String header = request.getHeader("Content-Type");
                            ContentType parse = ContentType.parse(header);

                            //文件上传逻辑
                            if (ContentType.MULTIPART_FORM_DATA.isSameMimeType(parse) && ALLOW_UPDATE) {
                                if (!LocationMapping.MULTIPART_BEAN_RESOURCE_MAPPING.containsKey(request.getUrl().split("[?]")[0])) {
                                    socketChannel.shutdownInput();
                                    return $401_BAD_REQUEST.putHeaderDate(ZonedDateTime.now()).setAssemble(true);
                                }
                                fileUpload(request, socketChannel, parse, all, efIdx, currentContentLength, contentLength);

                            } else {
                                long ll = contentLength - currentContentLength;
                                //去除多余的数据
                                if (contentLength != -1) {
                                    temp = IoUtil.readBytes2(socketChannel, (int) ll);
                                    byte[] body = new byte[(int) contentLength];
                                    System.arraycopy(all, efIdx, body, 0, currentContentLength);
                                    System.arraycopy(temp, 0, body, currentContentLength, (int) ll);
                                    request.setBody(body);
                                    request.setParseEnd();
                                }

                            }
                        }

                    }
                }
            }
        } while (!request.isParseEnd() && all.length != 0);
        return request;
    }

    static void assembleHeader(Request request, byte[] header, SocketChannel socketChannel) throws Exception {
        //只剩body
        ArrayList<byte[]> info$header = ArrayUtil.splitByTarget(header, RN);
        ArrayList<byte[]> info = splitByTarget(info$header.remove(0), SPACE);

        if (info.size() < 3) {
            socketChannel.close();
            throw new RebuildSelectorException("消息解析异常");
        }
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


    /**
     * 文件上传逻辑
     *
     * @author YYJ
     * @version 1.0.0
     * @description
     */
    static void fileUpload(Request request, SocketChannel socketChannel, ContentType parse, byte[] all, int efIdx, int currentContentLength, long contentLength) throws IOException {
        String boundary = "--" + parse.getParameter("boundary") + "\r\n";
        byte[] boundaryBytes = boundary.getBytes();
        byte[] temp = ArrayUtils.subarray(all, efIdx, all.length);

        Stack<MultipartFile> multipartFileStack = new Stack<>();
        while (currentContentLength < contentLength) {
            //Multipart
            ArrayList<byte[]> boundarys = splitByTarget(temp, boundaryBytes);
            if (boundarys.size() > 0 || multipartFileStack.size() > 0) {
                for (int i = 0; i < boundarys.size(); i++) {
                    if (i == 0 && multipartFileStack.size() > 0) {
                        multipartFileStack.peek().write(boundarys.get(0));
                    } else {
                        ArrayList<byte[]> bytes = splitByTarget(boundarys.get(i), RN_RN);
                        if (bytes.size() > 0) {
                            byte[] multiHeaderBytes = bytes.get(0);
                            ArrayList<byte[]> multiHeader = splitByTarget(multiHeaderBytes, RN);
                            if (multiHeader.size() == 2) {
                                String Content_Disposition = new String(multiHeader.get(0), StandardCharsets.UTF_8);
                                String[] Content_Dispositions = Content_Disposition.split("filename=\"");
                                String fileName = StringUtil.removeEnd(Content_Dispositions[1], "\"");
                                MultipartFile multipartFile = new MultipartFile(fileName, "/tmp");
                                multipartFileStack.push(multipartFile);
                                if (bytes.size() == 2)
                                    multipartFileStack.peek().write(bytes.get(1));
                            }

                        }
                    }
                }
            }
            temp = IoUtil.readBytes2(socketChannel, (int) DEFAULT_BUF_LENGTH * 2);
            currentContentLength += temp.length;
        }
        request.setMultipartFile(multipartFileStack.pop().endWrite());
        request.setParseEnd();
    }

    static void assembleMultipartFileHeader() {

    }

}
