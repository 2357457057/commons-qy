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


    /**
     *
     * @param name 当前线程名称
     * @author YYJ
     * @description 设置当前线程名称
     * */
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

        if (StringUtils.isBlank(poolName))
            poolName = "Pool";

        if (StringUtils.isBlank(threadName))
            threadName = "th";


        return new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new QyPoolNameFactory().QyThreadFactory(poolName, threadName));
    }


    /**
     * @param size       线程数
     * @param poolName   池名
     * @param threadName 线程名
     * @author YYJ
     * @description 创建任务线程池
     */
    public static ScheduledThreadPoolExecutor createScheduledPool(int size, String poolName, String threadName) {
        return new ScheduledThreadPoolExecutor(size, new QyPoolNameFactory().QyThreadFactory(poolName, threadName), new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * @param iniDelay 启动时间
     * @param period   间隔时间
     * @param unit     时间单位
     * @param runnable run obj
     * @author YYJ
     * @description 创建定时任务
     */
    public static void createPeriodScheduled(long iniDelay, long period, TimeUnit unit, Runnable runnable) {
        createScheduledPool(1, "Scheduled", "").scheduleAtFixedRate(runnable, iniDelay, period, unit);
    }
}
