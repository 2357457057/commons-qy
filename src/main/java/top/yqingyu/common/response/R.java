package top.yqingyu.common.response;

import top.yqingyu.common.exception.QyErrorInfoInterface;
import top.yqingyu.common.exception.QyRuntimeException;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

/**
 * 勿用fastjson进行反序列化 * 如需反序列化，请用jackson。
 * e.g. R o = new ObjectMapper().createParser(objectMapper.writeValueAsString(R)).readValueAs(new TypeReference() {}); * *
 *
 * @author qingyu
 */
public class R extends HashMap<String, Object> {

    public static final String DATA = "data";
    public static final String LIST = "list";
    public static final String RSP_CODE = "rspCode";
    public static final String RSP_MSG = "rspMsg";

    public static final String RSP_CODE_SUCC = "000";
    public static final String RSP_MSG_SUCC = "success";
    public static final String RSP_CODE_FAIL = "999";
    public static final String RSP_MSG_FAIL = "fail";
    public static final String RSP_CODE_ERROR = "500";
    public static final String RSP_MSG_ERROR = "error";

    private static final HashMap<String, Boolean> keyWord = new HashMap<>() {
        @Serial
        private static final long serialVersionUID = -6548363057529297192L;

        {
            put(RSP_CODE, true);
            put(RSP_MSG, true);
        }
    };
    public static final Type TYPE = new Type();
    @Serial
    private static final long serialVersionUID = 1165937145498782077L;


    private R() {
    }

    public static R instance() {
        R r = new R();
        return r.setCodeAndMsg(RSP_CODE_FAIL, RSP_MSG_FAIL);
    }

    public static R instance(String code, String msg) {
        return instance().setCodeAndMsg(code, msg);
    }

    public static R instance(QyErrorInfoInterface errorEnum) {
        return instance(errorEnum.getRspCode(), errorEnum.getRspMsg());
    }

    public static R ok() {
        return instance().setCodeAndMsg(RSP_CODE_SUCC, RSP_MSG_SUCC);
    }

    public static R ok(Object data) {
        return ok().put(DATA, data);
    }

    public static R ok(String key, Object data) {
        return ok().put(key, data);
    }

    public static R error() {
        return instance();
    }

    public static R error(Object data) {
        return error().put(DATA, data);
    }

    public static R error(String key, Object data) {
        return error().put(key, data);
    }

    public static R errorMsg(String msg) {
        return instance().setRspMsg(msg);
    }

    public R setOk() {
        return this.setCodeAndMsg(RSP_CODE_SUCC, RSP_MSG_SUCC);
    }

    public R setOk(Object data) {
        return this.setOk().put(DATA, data);
    }

    public R setOk(String key, Object data) {
        return this.setOk().put(key, data);
    }

    public R setError() {
        return this.setCodeAndMsg(RSP_CODE_FAIL, RSP_MSG_FAIL);
    }

    public R setFail(Object data) {
        return this.setError().put(DATA, data);
    }

    public R setFail(String key, Object data) {
        return this.setError().put(key, data);
    }

    public R put(String key, Object value) {
        if (keyWord.containsKey(key)) {
            throw new QyRuntimeException("返回值装填失败，与 {} 冲突", key);
        }
        super.put(key, value);
        return this;
    }

    public R putAllS(Map<String, Object> m) {
        super.putAll(m);
        return this;
    }

    public R setRspCode(String rspCode) {
        super.put(RSP_CODE, rspCode);
        return this;
    }

    public R setRspMsg(String rspMsg) {
        super.put(RSP_MSG, rspMsg);
        return this;
    }

    public R setCodeAndMsg(String rspCode, String rspMsg) {
        super.put(RSP_CODE, rspCode);
        super.put(RSP_MSG, rspMsg);
        return this;
    }

    public R setData(Object data) {
        return this.put(DATA, data);
    }

    public R setList(Object list) {
        return this.put(LIST, list);
    }


    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Type type) {
        return (T) get(key);
    }

    public String RspCode() {
        return get(RSP_CODE, TYPE);
    }

    public String RspMsg() {
        return this.get(RSP_CODE, TYPE);
    }


    public <T> T Data() {
        return this.get(DATA, TYPE);
    }

    private static class Type {
    }
}