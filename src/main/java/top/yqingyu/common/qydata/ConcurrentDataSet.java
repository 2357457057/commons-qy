package top.yqingyu.common.qydata;


import java.io.Serial;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.qydata.ConcurrentDataSet
 * @description
 * @createTime 2022年09月17日 16:16:00
 */
@SuppressWarnings("all")
public class ConcurrentDataSet<E> extends AbstractSet<E> implements Serializable {
    @Serial
    private static final long serialVersionUID = 7997886765361607472L;
    private static final Boolean PRESENT = true;
    private final ConcurrentHashMap<E, Boolean> map;

    public ConcurrentDataSet() {
        this.map = new ConcurrentHashMap();
    }

    public ConcurrentDataSet(int initialCapacity) {
        this.map = new ConcurrentHashMap(initialCapacity);
    }

    public ConcurrentDataSet(int initialCapacity, float loadFactor) {
        this.map = new ConcurrentHashMap(initialCapacity, loadFactor);
    }

    public ConcurrentDataSet(int initialCapacity, float loadFactor, int concurrencyLevel) {
        this.map = new ConcurrentHashMap(initialCapacity, loadFactor, concurrencyLevel);
    }

    public ConcurrentDataSet(Iterable<E> iter) {
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
        return this.map.put(e, PRESENT) == null;
    }

    public boolean remove(Object o) {
        return PRESENT.equals(this.map.remove(o));
    }

    public void clear() {
        this.map.clear();
    }
}
