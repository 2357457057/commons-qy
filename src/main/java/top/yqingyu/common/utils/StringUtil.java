package top.yqingyu.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.utils.StringUtil
 * @description
 * @createTime 2022年09月04日 15:41:00
 */
public class StringUtil {
    /**
     *
     * 根据给定的中文字节长度，
     * 以及目标数据库所占字节数，
     * 来截取string
     * 防止将数据库字段撑爆
     * @auth yyj
     * date 2022/7/13 23:51
     *
     */
    public static String getChineseStringBytesSub(String str, int value_size, int char_size) {
        if (str.length() > value_size / char_size) {
            char[] chars = str.toCharArray();
            int length = 0;
            int i = 0;
            for (char aChar : chars) {
                if (String.valueOf(aChar).matches("[^\\x00-\\xff]")) {
                    length += char_size - 1;
                }
                length++; // oracle中文三字节

                if (length > value_size)
                    break;
                i++;
            }
            str = StringUtils.substring(str, 0, i);
        }
        return str;
    }
}
