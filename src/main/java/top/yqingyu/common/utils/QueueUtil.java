package top.yqingyu.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.utils.QueueUtil
 * @description
 * @createTime 2022年08月23日 19:24:00
 */
public class QueueUtil {
    /**
     * 取出queue中的一定量的對象，
     * 不足最大值取出全部，超最大值取最大值
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
