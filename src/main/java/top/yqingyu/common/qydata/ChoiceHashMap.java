package top.yqingyu.common.qydata;

import java.io.Serial;
import java.util.HashMap;

/**
 * * 将会有一个默认值的键值对
 * * 非线程安全.
 * * 考虑到性能以及作为选择器使用，应该不会出现同时写入的操作。
 *
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.qydata.ChoiceHashMap
 * @createTime 2023年01月09日 23:29:00
 */
public class ChoiceHashMap<A, B> extends HashMap<A, B> {
    @Serial
    private static final long serialVersionUID = 3105059776160149805L;
    private A defaultKey;
    private B defaultValue;

    @Override
    public B get(Object key) {
        B b = super.get(key);
        if (null == b) {
            return defaultValue;
        }
        return b;
    }

    public void putDft(A key, B value) {
        if (null != super.get(key)) super.remove(defaultKey);
        super.put(key, value);
        defaultKey = key;
        defaultValue = value;
    }

}
