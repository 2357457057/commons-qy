package top.yqingyu.common.nio$server.core;

import java.io.Serial;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.core.OperatingRecorder
 * @description 为解决Epoll 问题而生
 * @createTime 2022年09月20日 00:30:00
 */
public class OperatingRecorder<E> extends AbstractSet<E> implements Serializable {

    @Serial
    private static final long serialVersionUID = 7997886765361607471L;
    private final Integer RepeatedUpperLimit;
    private final ConcurrentHashMap<E, AtomicInteger> map;

    public OperatingRecorder(Integer repeatedUpperLimit) {
        RepeatedUpperLimit = repeatedUpperLimit;
        this.map = new ConcurrentHashMap<>();
    }

    public OperatingRecorder(Integer repeatedUpperLimit, int initialCapacity) {
        RepeatedUpperLimit = repeatedUpperLimit;
        this.map = new ConcurrentHashMap<>(initialCapacity);
    }

    public OperatingRecorder(Integer repeatedUpperLimit, int initialCapacity, float loadFactor) {
        RepeatedUpperLimit = repeatedUpperLimit;
        this.map = new ConcurrentHashMap<>(initialCapacity, loadFactor);
    }

    public OperatingRecorder(Integer repeatedUpperLimit, int initialCapacity, float loadFactor, int concurrencyLevel) {
        RepeatedUpperLimit = repeatedUpperLimit;
        this.map = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrencyLevel);
    }

    public OperatingRecorder(Iterable<E> iter, Integer repeatedUpperLimit) {
        RepeatedUpperLimit = repeatedUpperLimit;
        if (iter instanceof Collection<E> collection) {
            this.map = new ConcurrentHashMap<>((int) ((float) collection.size() / 0.75F));
            this.addAll(collection);
        } else {
            this.map = new ConcurrentHashMap<>();
            for (E e : iter) {
                this.add(e);
            }
        }

    }

    public Iterator<E> iterator() {
        return this.map.keySet().iterator();
    }

    public int size() {
        return this.map.size();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public boolean contains(Object o) {
        return this.map.containsKey(o);
    }

    public boolean add(E e) {
        if (this.map.containsKey(e)) {
            if ((map.get(e).getAndIncrement() > 512)) {
                throw new ExceedingRepetitionLimitException("该值重复达上限");
            }
            return false;
        } else {
            this.map.put(e, new AtomicInteger());
            return true;
        }
    }

    public boolean remove(Object o) {
        return this.map.remove(o) == null;
    }

    public void clear() {
        this.map.clear();
    }
}
