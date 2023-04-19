package top.yqingyu.common.utils;


public class ChineseUtil {
    public static boolean isChinese(char c) {
        return c == 12295 || (c >= 19968 && c <= 40869);
    }

    public static boolean isChinese(String c) {
        char[] chars = c.toCharArray();
        for (char aChar : chars) {
            if (!isChinese(aChar)) {
                return false;
            }
        }
        return true;
    }
}
