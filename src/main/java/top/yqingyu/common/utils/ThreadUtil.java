package top.yqingyu.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YYJ
 * @version 1.0.0
 * @date 2022/7/9 22:33
 * @description
 * @modified by
 */
public class ThreadUtil {


    public final static class QyPoolNameFactory implements ThreadFactory {

        private static final AtomicInteger poolNumber = new AtomicInteger(0);
        private ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private String namePrefix;

        public QyPoolNameFactory() {
        }

        @SuppressWarnings("removal")
        ThreadFactory QyThreadFactory(String poolName, String threadName) {


            StringBuilder sb = new StringBuilder("QyPool");

            SecurityManager s = System.getSecurityManager();

            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();

            sb.append(poolNumber.getAndIncrement())
                    .append("-")
                    .append(poolName)
                    .append("-")
                    .append(threadName)
                    .append("-");

            namePrefix = sb.toString();
            return this;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }


    public static ThreadPoolExecutor createQyFixedThreadPool(int size, String poolName, String threadName) {

        if (StringUtils.isBlank(poolName))
            poolName = "Pool";

        if (StringUtils.isBlank(threadName))
            threadName = "th";


        return new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new QyPoolNameFactory().QyThreadFactory(poolName, threadName));
    }


    public static void setThisThreadName(String name) {
        Thread thread = Thread.currentThread();
        thread.setName(name);
    }
}
