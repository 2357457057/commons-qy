import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName PACKAGE_NAME.sss
 * @description
 * @createTime 2023年01月05日 00:25:00
 */
public class sss {

    static ArrayList<Integer> aaa;

    public static int n(Integer... args) {
        aaa = new ArrayList<>(args.length);
        aaa.addAll(List.of(args));

        int score = 0;
        // 熊最小的连续的开始移除。

        return score;
    }

    //统计数量及下标

    static HashMap<Integer, ArrayList<Integer>> bb() {
        HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
        for (int i = 0; i < aaa.size(); i++) {
            Integer integer = aaa.get(i);
            ArrayList<Integer> idxL = map.get(integer);
            if (idxL != null) {
                idxL.add(i);
            } else {
                map.put(integer, new ArrayList<>(List.of(integer)));
            }
        }
        return map;
    }
}
