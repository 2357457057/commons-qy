package top.yqingyu.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YYJ
 * @version 1.0.0
 * @date 2022/7/9 22:33
 * @description
 * @modified by
 */
public class ThreadUtil {


    public final static class QyGlobalPoolNameFactory implements ThreadFactory {

        private static final AtomicInteger poolNumber = new AtomicInteger(0);
        private ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private String namePrefix;

        public QyGlobalPoolNameFactory() {
        }

        @SuppressWarnings("removal")
        ThreadFactory QyThreadFactory(String poolName, String threadName) {


            StringBuilder sb = new StringBuilder("Pool");

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

    public final static class QyCurrentPoolNameFactory implements ThreadFactory {

        private ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private String namePrefix;

        public QyCurrentPoolNameFactory() {
        }

        @SuppressWarnings("removal")
        public ThreadFactory QyThreadFactory(String poolName, String threadName) {


            StringBuilder sb = new StringBuilder();

            SecurityManager s = System.getSecurityManager();

            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();

            sb
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

    /**
     * @param name ??????????????????
     * @author YYJ
     * @description ????????????????????????
     */
    public static void setThisThreadName(String name) {
        Thread thread = Thread.currentThread();
        thread.setName(name);
    }

    /**
     * @param size       ?????????
     * @param poolName   ??????
     * @param threadName ?????????
     * @author YYJ
     * @description ???????????????????????????
     */
    public static ThreadPoolExecutor createQyFixedThreadPool(int size, String poolName, String threadName) {

        if (StringUtils.isBlank(poolName))
            poolName = "Pool";

        if (StringUtils.isBlank(threadName))
            threadName = "th";


        return new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new QyGlobalPoolNameFactory().QyThreadFactory(poolName, threadName));
    }

    /**
     * @param size ?????????
     * @author YYJ
     * @description ???????????????????????????
     */
    public static ThreadPoolExecutor createQyFixedThreadPool(int size, ThreadFactory threadFactory) {

        return new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
    }

    /**
     * @param size       ?????????
     * @param poolName   ??????
     * @param threadName ?????????
     * @author YYJ
     * @description ?????????????????????
     */
    public static ScheduledThreadPoolExecutor createScheduledPool(int size, String poolName, String threadName) {
        return new ScheduledThreadPoolExecutor(size, new QyGlobalPoolNameFactory().QyThreadFactory(poolName, threadName), new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * @param iniDelay ????????????
     * @param period   ????????????
     * @param unit     ????????????
     * @param runnable run obj
     * @author YYJ
     * @description ??????????????????
     */
    public static void createPeriodScheduled(long iniDelay, long period, TimeUnit unit, Runnable runnable) {
        createScheduledPool(1, "Scheduled", "").scheduleAtFixedRate(runnable, iniDelay, period, unit);
    }
}
