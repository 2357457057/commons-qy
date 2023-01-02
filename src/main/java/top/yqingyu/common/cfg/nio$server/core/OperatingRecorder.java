package top.yqingyu.common.cfg.nio$server.core;

import java.io.Serial;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.core.OperatingRecorder
 * @description 计数器，确认计数器
 * @createTime 2022年09月20日 00:30:00
 */
public class OperatingRecorder<E> extends AbstractSet<E> implements Serializable {

    @Serial
    private static final long serialVersionUID = 7997886765361607471L;
    private Long MaxRepeat;
    private ConcurrentHashMap<E, AtomicLong> map;

    private ConcurrentHashMap<E, ACK> ackMap;


    private OperatingRecorder() {
    }

    public static OperatingRecorder<Integer> createNormalRecorder(Long maxRepeat) {
        OperatingRecorder<Integer> objects = new OperatingRecorder<>();
        objects.map = new ConcurrentHashMap<>();
        objects.MaxRepeat = maxRepeat;
        return objects;
    }

    public static OperatingRecorder<Integer> createAckRecorder(Long maxRepeat) {
        OperatingRecorder<Integer> objects = new OperatingRecorder<>();
        objects.ackMap = new ConcurrentHashMap<>();
        objects.MaxRepeat = maxRepeat - 1;
        return objects;
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

    public AtomicLong add2(E e) {
        AtomicLong b = this.map.get(e);
        if (b != null) {
            if (b.getAndIncrement() > MaxRepeat) {
                throw new RebuildSelectorException("该值重复达上限" + b.get());
            }
            return b;
        } else {
            AtomicLong atomicLong = new AtomicLong();
            this.map.put(e, atomicLong);
            return atomicLong;
        }
    }

    public boolean remove(Object o) {
        return this.map.remove(o) == null;
    }

    public void clear() {
        this.map.clear();
    }


    public void addAck(E e) {
        ACK ack = this.ackMap.get(e);
        if (ack == null) {
            this.ackMap.put(e, new ACK());
        } else {
            Long now = ack.getNow();
            if (now < MaxRepeat) {
                ack.add();
            } else {
                throw new RebuildSelectorException("ACK 添加上限");
            }
        }
    }

    public boolean ack(E l) {
        ACK ack = this.ackMap.get(l);
        if (ack != null) {
            ack.ack();
            return true;
        } else
            return false;
    }

    public boolean isAckOk(E l) {
        ACK ack = this.ackMap.get(l);
        return ack != null && ack.isAckOk();
    }

    public void removeAck(E e) {
        this.ackMap.remove(e);
    }

    static class ACK {
        private final AtomicLong CEILING = new AtomicLong(1);
        private final AtomicLong ACK = new AtomicLong(1);
        private final ReentrantLock lock = new ReentrantLock();

        private volatile boolean ackOk = false;

        void add() {
            try {
                lock.lock();
                CEILING.getAndIncrement();
                ACK.getAndIncrement();
            } finally {
                lock.unlock();
            }
        }

        void ack() {
            try {
                lock.lock();
                long get = ACK.decrementAndGet();
                if (get <= 0) {
                    ackOk = true;
                }
            } finally {
                lock.unlock();
            }
        }

        Long getNow() {
            return this.CEILING.get();
        }

        boolean isAckOk() {
            return ackOk;
        }
    }

}
