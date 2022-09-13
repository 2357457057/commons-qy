package top.yqingyu.common.nio$server.event$http.entity;

import cn.hutool.core.date.LocalDateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import top.yqingyu.common.qydata.DataMap;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$http.entity.Response
 * @description
 * @createTime 2022年09月13日 22:10:00
 */
public class Response {
    private HttpVersion httpVersion;
    private String state_code;
    private final DataMap header = new DataMap();
    private String body;

    public Response putHeader(String key, String value) {
        header.put(key, value);
        return this;
    }

    public Response putHeaderToken(String value) {
        header.put("Token", value);
        return this;
    }

    public Response putHeaderServer(String value) {
        header.put("Server", "QingYu2.1");
        return this;
    }

    public Response putHeaderContentType(ContentType value) {
        header.put("Content-Type", value.toString());
        return this;
    }

    public Response putHeaderContentLength(long value) {
        header.put("Content-Length", String.valueOf(value));
        return this;
    }

    public Response putHeaderAcceptRanges() {
        header.put("Accept-Ranges", "bytes");
        return this;
    }

    public Response putHeaderDate(LocalDateTime ldt) {
        String s = LocalDateTimeUtil.format(ldt, "EEE, d MMM yyyy HH:mm:ss");
        header.put("Date", s);
        return this;
    }

    public void setHttpVersion(HttpVersion httpVersion) {
        this.httpVersion = httpVersion;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }


    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public String getState_code() {
        return state_code;
    }

    public DataMap getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(httpVersion.getV()).append(" ").append(state_code).append("\r\n");
        if (StringUtils.isNotBlank(body)) {
            this
                    .putHeaderContentType(ContentType.APPLICATION_JSON)
                    .putHeaderContentLength(body.getBytes(StandardCharsets.UTF_8).length);

        }

        header.forEach((k, v) -> sb.append(k).append(":").append(" ").append(v).append("\r\n"));
        sb.append("\r\n");

        if (StringUtils.isNotBlank(body)) {
            sb.append(body);
        }
        return sb.toString();
    }
}
