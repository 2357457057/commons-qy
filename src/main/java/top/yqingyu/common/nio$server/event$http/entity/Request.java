package top.yqingyu.common.nio$server.event$http.entity;

import com.alibaba.fastjson2.JSON;
import top.yqingyu.common.qydata.DataMap;

import java.nio.charset.StandardCharsets;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$http.entity.Request
 * @description
 * @createTime 2022年09月09日 22:02:00
 */
public class Request {


    private HttpMethod method;

    private HttpVersion httpVersion;

    private String url;

    private final DataMap header = new DataMap();

    private String ContentType;

    private byte[] body;


    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(byte[] method) {
        this.method = Request.HttpMethod.getMethod(new String(method, StandardCharsets.UTF_8));
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(HttpVersion httpVersion) {
        this.httpVersion = httpVersion;
    }

    public void setHttpVersion(byte[] httpVersion) {
        this.httpVersion = Request.HttpVersion.getVersion(new String(httpVersion, StandardCharsets.UTF_8));
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrl(byte[] url) {
        this.url = new String(url, StandardCharsets.UTF_8);
    }

    public DataMap getHeader() {
        return header;
    }

    public void putHeader(String key, Object obj) {
        this.header.put(key, obj);
    }

    public void putHeader(byte[] key, byte[] obj) {
        this.header.put(new String(key, StandardCharsets.UTF_8), new String(obj, StandardCharsets.UTF_8));
    }


    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public enum HttpMethod {
        GET,
        POST,

        /**
         * Supported Methods
         */
        OPTIONS,

        /**
         * return a demo head
         */
        HEAD,
        /**
         * Uploading resources
         */
        PUT,
        /**
         * Delete the resource
         */
        DELETE,

        /**
         * Echo-Request
         */
        TRACE,

        /**
         * I guess it's to support SSL, I don't know
         */
        CONNECT;

        public static HttpMethod getMethod(String m) {


            if (GET.name().equals(m)) return GET;
            if (POST.name().equals(m)) return POST;
            if (OPTIONS.name().equals(m)) return OPTIONS;
            if (HEAD.name().equals(m)) return HEAD;
            if (PUT.name().equals(m)) return PUT;
            if (DELETE.name().equals(m)) return DELETE;
            if (TRACE.name().equals(m)) return TRACE;
            if (CONNECT.name().equals(m)) return CONNECT;

            else return null;


        }
    }

    public enum HttpVersion {

        V_1_1("HTTP/1.1"),
        V_2("HTTP/2"),

        V_3("HTTP/3");
        private final String v;

        HttpVersion(String v) {
            this.v = v;
        }

        public static HttpVersion getVersion(String v) {
            if (V_1_1.v.equals(v)) return V_1_1;
            if (V_2.v.equals(v)) return V_2;
            if (V_3.v.equals(v)) return V_3;
            else return null;
        }
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
