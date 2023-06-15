package top.yqingyu.common.qydata;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 继承 {@link ConcurrentHashMap} 可以根据指定的数据类型进行转换
 *
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.qydata.ConcurrentQyMap
 * @description
 * @createTime 2022年12月21日 23:24:00
 */
public class ConcurrentQyMap<A, B> extends ConcurrentHashMap<A, B> implements Serializable {
    @Serial
    private static final long serialVersionUID = 4115577532915649244L;

    /**
     * @param key the key whose associated value is to be returned
     * @return
     */
    public <T> T get(Object key, Class<T> type) {
        Object o = super.get(key);
        try {
            return (T) o;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 连续put
     */
    public ConcurrentQyMap<A, B> putConsecutive(A a, B b) {
        super.put(a, b);
        return this;
    }
}
