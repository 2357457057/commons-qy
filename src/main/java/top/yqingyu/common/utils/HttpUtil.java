package top.yqingyu.common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;
import okio.Timeout;
import top.yqingyu.common.exception.IllegalMediaTypeException;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class HttpUtil {

    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=UTF-8");

    /**
     * get
     */
    static OkHttpClient httpclient = new OkHttpClient();

    public static JSONObject doGet(String url, Map<String, String> headers, Map<String, String> urlParam) throws Exception {
        Request.Builder builder = new Request.Builder().get();
        StringBuilder sb = new StringBuilder(url);
        boolean contains = url.contains("?");
        if (null != urlParam) {
            AtomicInteger integer = new AtomicInteger();
            urlParam.forEach((a, b) -> {
                if (integer.getAndIncrement() == 0 && !contains) {
                    sb.append("?");
                } else if (sb.charAt('?') != sb.length() - 1) {
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
        ResponseBody body = execute.body();

        MediaType contentType = body.contentType();
        if (JSON_TYPE.equals(contentType)) {
            String string = body.string();
            return JSON.parseObject(string);
        } else {
            throw new IllegalMediaTypeException("不支持的媒体类型");
        }
    }

    public static JSONObject doPost(String url, Map<String, String> headers, Map<String, String> urlParam, Object body) throws Exception {
        OkHttpClient httpclient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON.toJSONString(body), JSON_TYPE);
        Request.Builder builder = new Request.Builder();
        StringBuilder sb = new StringBuilder(url);
        boolean contains = url.contains("?");
        if (null != urlParam) {
            AtomicInteger integer = new AtomicInteger();
            urlParam.forEach((a, b) -> {
                if (integer.getAndIncrement() == 0 && !contains) {
                    sb.append("?");
                } else if (sb.charAt('?') != sb.length() - 1) {
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
        ResponseBody responseBody = execute.body();
        MediaType contentType = responseBody.contentType();
        if (JSON_TYPE.equals(contentType)) {
            String string = responseBody.string();
            return JSON.parseObject(string);
        } else {
            throw new IllegalMediaTypeException("不支持的媒体类型");
        }
    }
}
