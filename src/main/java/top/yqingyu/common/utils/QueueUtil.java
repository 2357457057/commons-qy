package top.yqingyu.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.utils.QueueUtil
 * @description
 * @createTime 2022年08月23日 19:24:00
 */
public class QueueUtil {
    /**
     *
     * 取出{@link BlockingQueue<T>}中的一定量的對象，
     * 超最大值取最大值,不足最小值取最小值
     * date 2022/7/15 17:58
     */
    public static <T> List<T> popBlockingQueue(BlockingQueue<T> queue, int min, int max) throws InterruptedException {

        int mini = Math.max(queue.size(),min);
        int pop = Math.min(mini, max);

        ArrayList<T> list = new ArrayList<>();
        for (int i = 0; i < pop; i++) {
            T poll = queue.take();
            if (poll == null)
                break;
            list.add(poll);
        }
        return list;

    }

    /**
     *
     * 取出{@link BlockingQueue<T>}中的一定量的對象，
     * 超最大值取最大值,不足最小值取最小值
     * date 2022/7/15 17:58
     */
    public static <T> List<T> popBlockingQueue(BlockingQueue<T> queue, int min, int max, long timeout, TimeUnit unit) throws InterruptedException {

        int mini = Math.max(queue.size(),min);
        int pop = Math.min(mini, max);

        ArrayList<T> list = new ArrayList<>();
        for (int i = 0; i < pop; i++) {
            T poll = queue.poll(timeout,unit);
            if (poll == null)
                break;
            list.add(poll);
        }
        return list;

    }


    /**
     * 取出{@link Queue<T>}中的一定量的對象，
     * 不足最大值取出全部，超最大值取最大值
     * 注意！{@link BlockingQueue<T>.poll()}
     * date 2022/7/15 17:58
     */
    public static <T> List<T> popQueue(Queue<T> queue, int max) {
        int pop = Math.min(queue.size(), max);
        ArrayList<T> list = new ArrayList<>();
        for (int i = 0; i < pop; i++) {
            T poll = queue.poll();
            if (poll == null)
                break;
            list.add(poll);
        }
        return list;

    }

}
