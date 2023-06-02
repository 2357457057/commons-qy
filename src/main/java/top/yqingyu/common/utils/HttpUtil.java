package top.yqingyu.common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;
import okio.Timeout;
import top.yqingyu.common.exception.IllegalMediaTypeException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于OKHTTP3 封装的http请求工具类
 */
public class HttpUtil {

    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=UTF-8");

    /**
     * get
     */
    static volatile OkHttpClient httpclient = new OkHttpClient();


    public static void initUnsafe() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.setHostnameVerifier$okhttp((s, sslSession) -> true);
        httpclient = builder.build();
    }


    public static JSONObject doGet(String url, Map<String, String> headers, Map<String, String> urlParam) throws Exception {
        ResponseBody body = getPub(url, headers, urlParam);
        MediaType contentType = body.contentType();
        if (JSON_TYPE.type().equals(contentType.type())) {
            String string = body.string();
            return JSON.parseObject(string);
        } else {
            throw new IllegalMediaTypeException("不支持的媒体类型");
        }
    }

    public static String doGet2(String url, Map<String, String> headers, Map<String, String> urlParam) throws Exception {
        ResponseBody body = getPub(url, headers, urlParam);
        return body.string();
    }

    public static byte[] doGet3(String url, Map<String, String> headers, Map<String, String> urlParam) throws Exception {
        ResponseBody body = getPub(url, headers, urlParam);
        return body.bytes();
    }

    public static JSONObject doPost(String url, Map<String, String> headers, Map<String, String> urlParam, Object body) throws Exception {
        return doPost(url, headers, urlParam, body, JSON_TYPE);
    }

    public static JSONObject doPost(String url, Map<String, String> headers, Map<String, String> urlParam, Object body, MediaType requestType) throws Exception {
        if (requestType.equals(JSON_TYPE)) {
            body = JSON.toJSONString(body);
        }
        ResponseBody responseBody = postPub(url, headers, urlParam, body, requestType);
        MediaType contentType = responseBody.contentType();
        if (JSON_TYPE.type().equals(contentType.type())) {
            String string = responseBody.string();
            return JSON.parseObject(string);
        } else {
            throw new IllegalMediaTypeException("不支持的媒体类型");
        }
    }

    public static String doPost1(String url, Map<String, String> headers, Map<String, String> urlParam, Object body, MediaType requestType) throws Exception {
        ResponseBody responseBody = postPub(url, headers, urlParam, body, requestType);
        return responseBody.string();
    }

    public static byte[] doPost2(String url, Map<String, String> headers, Map<String, String> urlParam, Object body, MediaType requestType) throws Exception {
        ResponseBody responseBody = postPub(url, headers, urlParam, body, requestType);
        return responseBody.bytes();
    }

    public static ResponseBody getPub(String url, Map<String, String> headers, Map<String, String> urlParam) throws IOException {
        Request.Builder builder = new Request.Builder().get();
        StringBuilder sb = new StringBuilder(url);
        boolean contains = url.contains("?");
        if (null != urlParam) {
            AtomicInteger integer = new AtomicInteger();
            urlParam.forEach((a, b) -> {
                if (integer.getAndIncrement() == 0 && !contains) {
                    sb.append("?");
                } else if (sb.lastIndexOf("?") != sb.length() - 1) {
                    sb.append("&");
                }
                sb.append(a).append("=").append(b);

            });
        }
        if (null != headers) {
            headers.forEach(builder::addHeader);
        }
        builder.url(sb.toString());
        Request request = builder.build();
        Call call = httpclient.newCall(request);
        Timeout timeout = call.timeout();
        timeout.timeout(30, TimeUnit.SECONDS);
        Response execute = call.execute();
        return execute.body();
    }

    public static ResponseBody postPub(String url, Map<String, String> headers, Map<String, String> urlParam, Object body, MediaType requestType) throws IOException {
        RequestBody requestBody = RequestBody.create(body.toString(), requestType);
        Request.Builder builder = new Request.Builder();
        StringBuilder sb = new StringBuilder(url);
        boolean contains = url.contains("?");
        if (null != urlParam) {
            AtomicInteger integer = new AtomicInteger();
            urlParam.forEach((a, b) -> {
                if (integer.getAndIncrement() == 0 && !contains) {
                    sb.append("?");
                } else if (sb.lastIndexOf("?") != sb.length() - 1) {
                    sb.append("&");
                }
                sb.append(a).append("=").append(b);

            });
        }
        if (null != headers) {
            headers.forEach(builder::addHeader);
        }
        builder.url(sb.toString()).post(requestBody);
        Request request = builder.build();
        Call call = httpclient.newCall(request);
        Timeout timeout = call.timeout();
        timeout.timeout(30, TimeUnit.SECONDS);
        Response execute = call.execute();
        return execute.body();
    }
}
