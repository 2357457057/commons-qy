package top.yqingyu.common.cglib.core.internal;

public interface Function<K, V> {
    V apply(K key);
}
