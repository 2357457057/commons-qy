package top.yqingyu.common.qydata;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.qydata.ConcurrentQyMap
 * @description
 * @createTime 2022年12月21日 23:24:00
 */
public class ConcurrentQyMap<A, B> extends ConcurrentHashMap<A, B> {
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

    public ConcurrentQyMap<A, B> putConsecutive(A a, B b) {
        super.put(a, b);
        return this;
    }
}
