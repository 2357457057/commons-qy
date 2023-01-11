package top.yqingyu.common.utils;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.utils.UnameUtil
 * @description
 * @createTime 2022年09月17日 11:11:00
 */
public class UnameUtil {
    public static boolean isWindows() {
        return "\\".equals(System.getProperty("file.separator"));
    }
}
