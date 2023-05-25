package top.yqingyu.common.response;


import com.alibaba.fastjson2.JSON;
import top.yqingyu.common.exception.QyExceptionEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * web接口的统一响应对象 发送数据
 *
 * @author 杨永基
 * date: 2021/09/09
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", "0000");
        put("msg", "success");
    }

    public static R error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(400, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R error(String code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R error(int code) {
        R r = new R();
        r.put("code", code);
        r.put("msg", "");
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public static R ok(Object data) {
        return new R().put("data", data);
    }


    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public static R error(QyExceptionEnum e) {
        R r = new R();
        r.put("code", e.getResultCode());
        r.put("msg", e.getResultMsg());
        return r;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
