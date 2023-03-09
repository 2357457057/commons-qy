package top.yqingyu.common.utils;

import cn.hutool.core.util.RandomUtil;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 根据 输入的百分比返回 true or false
 *
 * @author YYJ
 * @version 1.0.0
 * @description
 * @createTime 2022年09月01日 00:18:00
 */
public class PercentUtil implements Runnable {


    public static final ArrayBlockingQueue<Integer> SendPercent = new ArrayBlockingQueue<Integer>(512);

    private static Thread thread = null;
    private static final int PERCENT = 10000;

    @Override
    public void run() {
        while (true) {
            try {
                int i = RandomUtil.randomInt(1, PERCENT + 1);
                SendPercent.put(i);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void init() {
        if (thread == null || !thread.isAlive() && SendPercent.size() == 0) {
            thread = new Thread(new PercentUtil());
            thread.start();
        }
    }

    public static boolean percentTrue(double percent) throws InterruptedException {
        init();
        if (percent > 100)
            throw new InterruptedException("百分比仅在0.01 ~ 100.00之间");
        if (percent < 0.01)
            throw new InterruptedException("百分比仅在0.01 ~ 100.00之间");
        Integer take = SendPercent.take();
        return take < PERCENT * percent / 100;
    }

    public static boolean percentFalse(double percent) throws InterruptedException {
        return !percentTrue(percent);
    }

}
