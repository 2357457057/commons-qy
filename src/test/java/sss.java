import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName PACKAGE_NAME.sss
 * @description
 * @createTime 2023年01月05日 00:25:00
 */
public class sss {
    int[] aaa;
    int score = 0;
    boolean run = true;
    boolean isFindMin = false;
    boolean needFindMin = false;
    AtomicInteger ninIndex = new AtomicInteger();

    /**
     * 其实数组中的数据分为几种类型
     * 1 单个的
     * 2 完全连续的     <--------
     * ^
     * 3 剔除后连续的           |
     * 4 无法连续的。(交叉的) --->
     * .....
     */
    public int cal(int... boxes) {
        aaa = boxes;
        // 熊最小的连续的开始移除。
        AtomicInteger haveContinuous = new AtomicInteger(0);

        do {
            Map<Integer, ArrayList<Integer>> group = group();
            haveContinuous.set(0);
            group.forEach((k, v) -> {
                if (isContinuous(v)) {
                    for (Integer integer : v) {
                        aaa[integer] = -1;
                    }
                    int size = v.size();
                    score += size * size;
                    haveContinuous.getAndIncrement();
                }
            });

            if (haveContinuous.get() == 0 && group.size() > 0) {
                if (isFindMin) {
                    ArrayList<Integer> list = group.get(ninIndex.get());
                    if (list != null)
                        calDiscreteData(list);
                    isFindMin = false;
                }
                needFindMin = true;
            }
        } while (run);
        return score;
    }

    //统计数量及下标
    Map<Integer, ArrayList<Integer>> group() {
        Map<Integer, ArrayList<Integer>> map = new HashMap<>();

        for (int i = 0; i < aaa.length; i++) {
            Integer integer = aaa[i];
            ArrayList<Integer> idxL = map.get(integer);
            if (idxL != null) {
                idxL.add(i);
            } else if (integer != -1) {
                map.put(integer, new ArrayList<>(List.of(i)));
            }
        }
        if (map.size() == 0) {
            run = false;
            return new HashMap<>();
        }
        if (needFindMin) {
            AtomicInteger size = new AtomicInteger();
            AtomicBoolean isFirst = new AtomicBoolean(true);
            map.forEach((a, b) -> {
                if (isFirst.get()) {
                    ninIndex.set(a);
                    size.set(b.size());
                    isFirst.set(false);
                } else if (b.size() < size.get()) {
                    ninIndex.set(a);
                    size.set(b.size());
                }
            });
            isFindMin = true;
            needFindMin = false;
        }
        return map;
    }

    //是连续的？
    boolean isContinuous(ArrayList<Integer> indexs) {
        if (indexs.size() == 1) {
            score += 1;
            aaa[indexs.get(0)] = -1;
            return false;
        }
        int currentValue = aaa[indexs.get(0)];

        int start = indexs.get(0);
        int end = indexs.get(indexs.size() - 1);
        for (int i = start + 1; i <= end; i++) {
            int val = aaa[i];
            if (!(val == -1 || val == currentValue)) {
                return false;
            }
        }
        return true;
    }

    //计算交叉数组
    void calDiscreteData(ArrayList<Integer> indexs) {
        int start = indexs.get(0);
        int end = indexs.get(indexs.size() - 1);
        int currentValue = aaa[indexs.get(0)];
        AtomicInteger cal = new AtomicInteger(0);

        int previous = 0;
        for (int i = start; i <= end; i++) {
            int val = aaa[i];
            if (start == i) {
                previous = val;
            }
            if (previous == -1 && val == currentValue) {
                aaa[i] = -1;
                cal.getAndIncrement();
            } else if (val == currentValue) {
                aaa[i] = -1;
                cal.getAndIncrement();
                if (i == end) {
                    score += cal.get() * cal.get();
                    cal.set(0);
                }
            } else {
                score += cal.get() * cal.get();
                cal.set(0);
            }
        }
    }

    int removeBoxes(int... boxes) {
        return new sss().cal(boxes);
    }

    public static void main(String[] args) {
        System.out.println((char) 110);
    }
}
