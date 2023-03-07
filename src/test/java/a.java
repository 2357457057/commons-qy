/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName PACKAGE_NAME.a
 * @description
 * @createTime 2023年02月24日 01:46:00
 */
class Solution {
    static int[][][] a;

    public static void main(String[] args) {
        System.out.println(removeBoxes(1, 2, 2, 1, 1, 1, 2, 1, 1, 2, 1, 2, 1, 1, 2, 2, 1, 1, 2, 2, 1, 1, 1, 2, 2, 2, 2, 1, 2, 1, 1, 2, 2, 1, 2, 1, 2, 2, 2, 2, 2, 1, 2, 1, 2, 2, 1, 1, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2, 1, 1, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 2, 2, 1));
    }

    public static int removeBoxes(int... boxes) {
        a = new int[boxes.length][boxes.length][boxes.length];
        return recursion(boxes, a, 0, boxes.length - 1, 0);
    }

    // arr[L...R]消除，而且前面跟着K个arr[L]这个数
    // 返回：所有东西都消掉，最大得分
    public static int recursion(int[] boxes, int[][][] dp, int left, int right, int k) {
        if (left > right) {
            return 0;
        }
        if (dp[left][right][k] > 0) {
            return dp[left][right][k];
        }
        // 找到开头，
        // 1,1,1,1,1,5
        // 3 4 5 6 7 8
        //         !
        int last = left;
        while (last + 1 <= right && boxes[last + 1] == boxes[left]) {
            last++;
        }
        // K个1     (k + last - left) last
        int pre = k + last - left;
        // 第一片连续的1直接合并，所以最后传0
        int ans = (pre + 1) * (pre + 1) + recursion(boxes, dp, last + 1, right, 0);
        for (int i = last + 2; i <= right; i++) {
            // boxes[i - 1] != boxes[left]排除连续的1中除第一个外的
            if (boxes[i] == boxes[left] && boxes[i - 1] != boxes[left]) {
                // last + 1, i - 1上自己玩没
                // i, right上和前面的pre+1个合并，这片新的1下一轮会走最开始的ans
                ans = Math.max(ans, recursion(boxes, dp, last + 1, i - 1, 0) + recursion(boxes, dp, i, right, pre + 1));
            }
        }
        if (ans == 2758)
            System.out.println(ans);
        dp[left][right][k] = ans;
        return ans;
    }


    static void printf(int[][][] a) {

    }
}