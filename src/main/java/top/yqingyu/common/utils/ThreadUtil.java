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


    public static ThreadFactory createThFactoryG(String poolName, String threadName) {
        return new QyGlobalPoolNameFactory().QyThreadFactory(poolName, threadName);
    }

    public static ThreadFactory createThFactoryC(String poolName, String threadName) {
        return new QyCurrentPoolNameFactory().QyThreadFactory(poolName, threadName);
    }

    /**
     * @param name 当前线程名称
     * @author YYJ
     * @description 设置当前线程名称
     */
    public static void setThisThreadName(String name) {
        Thread thread = Thread.currentThread();
        thread.setName(name);
    }

    /**
     * @param size       线程数
     * @param poolName   池名
     * @param threadName 线程名
     * @author YYJ
     * @description 创建阻塞无界线程池
     */
    public static ThreadPoolExecutor createQyFixedThreadPool(int size, String poolName, String threadName) {
        return createQyFixedThreadPool(size, size, 0L, poolName, threadName);
    }

    /**
     * @param coreSize   核心线程数
     * @param maxSize    最大线程数
     * @param poolName   池名
     * @param threadName 线程名
     * @author YYJ
     * @description 创建阻塞无界线程池
     */
    public static ThreadPoolExecutor createQyFixedThreadPool(int coreSize, int maxSize, long keepLive, String poolName, String threadName) {

        if (StringUtils.isBlank(poolName))
            poolName = "Pool";

        if (StringUtils.isBlank(threadName))
            threadName = "th";


        return new ThreadPoolExecutor(coreSize, maxSize, keepLive, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new QyGlobalPoolNameFactory().QyThreadFactory(poolName, threadName));
    }

    /**
     * @param size 线程数
     * @author YYJ
     * @description 创建阻塞无界线程池
     */
    public static ThreadPoolExecutor createQyFixedThreadPool(int size, ThreadFactory threadFactory) {

        return new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
    }

    /**
     * @param size       线程数
     * @param poolName   池名
     * @param threadName 线程名
     * @author YYJ
     * @description 创建任务线程池
     */
    public static ScheduledThreadPoolExecutor createScheduledPool(int size, String poolName, String threadName) {
        return new ScheduledThreadPoolExecutor(size, new QyGlobalPoolNameFactory().QyThreadFactory(poolName, threadName), new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * @param iniDelay 启动时间
     * @param period   间隔时间
     * @param unit     时间单位
     * @param runnable run obj
     * @author YYJ
     * @description 创建定时任务
     */
    public static ScheduledThreadPoolExecutor createPeriodScheduled(long iniDelay, long period, TimeUnit unit, Runnable runnable) {
        ScheduledThreadPoolExecutor scheduled = createScheduledPool(1, "Scheduled", "");
        scheduled.scheduleAtFixedRate(runnable, iniDelay, period, unit);
        return scheduled;
    }
}
