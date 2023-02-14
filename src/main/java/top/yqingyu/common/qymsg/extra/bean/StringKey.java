package top.yqingyu.common.qymsg.extra.bean;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.qymsg.extra.bean.StringKey
 * @description
 * @createTime 2023年02月14日 23:26:00
 */
public class StringKey extends KeyValue implements Serializable {
    @Serial
    private static final long serialVersionUID = 704921303243464809L;

    public StringKey() {
        super(DataType.STRING);
    }

    private String val;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }


    public static StringKey add(String key, String val) {
        return createKeyCmd(key, val, OperatingState.ADD);
    }

    public static StringKey get(String key) {
        return createKeyCmd(key, null, OperatingState.GET);
    }

    public static StringKey rm(String key) {
        return createKeyCmd(key, null, OperatingState.REMOVE);
    }

    public static StringKey createKeyCmd(String key, String val, KeyValue.OperatingState opeState) {
        StringKey stringKey = new StringKey();
        stringKey.setOperatingState(opeState);
        stringKey.setKey(key);
        stringKey.setVal(val);
        return stringKey;
    }
}
