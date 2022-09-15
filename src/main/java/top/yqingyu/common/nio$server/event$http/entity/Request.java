package top.yqingyu.common.nio$server.event$http.entity;

import com.alibaba.fastjson2.JSON;
import top.yqingyu.common.qydata.DataMap;

import java.net.URLDecoder;
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

    private final DataMap urlParam = new DataMap();
    private final DataMap header = new DataMap();
    private final DataMap cookie = new DataMap();

    private byte[] body;

    private boolean parseEnd = false;

    public boolean isParseEnd() {
        return parseEnd;
    }

    public void setParseEnd(boolean parseEnd) {
        this.parseEnd = parseEnd;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(byte[] method) {
        this.method = HttpMethod.getMethod(new String(method, StandardCharsets.UTF_8));
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
        this.httpVersion = HttpVersion.getVersion(new String(httpVersion, StandardCharsets.UTF_8));
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrl(byte[] url) {
        //中文解码
        this.url = URLDecoder.decode(new String(url, StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }

    public DataMap getHeader() {
        return header;
    }

    public String getHeader(String head) {
        return header.getString(head);
    }

    public void putHeader(String key, Object obj) {
        this.header.put(key, obj);
    }

    public DataMap getUrlParam() {
        return urlParam;
    }

    public String getUrlParam(String key) {
        return urlParam.getString(key);
    }

    public void putUrlParam(String key, Object obj) {
        this.urlParam.put(key, obj);
    }

    public DataMap getCookie() {
        return this.cookie;
    }

    public String getCookie(String cook) {
        return this.cookie.getString(cook);
    }

    public void putHeader(byte[] key, byte[] obj) {
        String keyStr = new String(key, StandardCharsets.UTF_8);
        String vStr = obj == null ? "" : new String(obj, StandardCharsets.UTF_8);
        if ("Cookie".equals(keyStr)) {
            String[] cookies = vStr.split("; ");
            for (String coo : cookies) {
                String[] split = coo.split("=");
                this.cookie.put(split[0], split[1]);
            }
        } else
            this.header.put(keyStr, vStr);
    }



    //防止body过大导致toString异常
    public byte[] getBody() {
        return null;
    }

    public byte[] gainBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
