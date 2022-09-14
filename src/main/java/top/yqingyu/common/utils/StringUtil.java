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
     * 根据给定的中文字节长度，
     * 以及目标数据库所占字节数，
     * 来截取string
     * 防止将数据库字段撑爆
     *
     * @auth yyj
     * date 2022/7/13 23:51
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


    /**
     *  有空值返回 false
     * @author YYJ
     * @description  */
    public static boolean equalsNull(final CharSequence cs1, final CharSequence cs2) {

        if (cs1 == null || cs2 == null) {
            return false;
        }

        if (cs1 == cs2) {
            return true;
        }
        if (cs1.length() != cs2.length()) {
            return false;
        }
        return regionMatches(cs1, true, 0, cs2, 0, cs1.length());
    }

    static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
                                 final CharSequence substring, final int start, final int length) {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        }
        int index1 = thisStart;
        int index2 = start;
        int tmpLen = length;

        // Extract these first so we detect NPEs the same as the java.lang.String version
        final int srcLen = cs.length() - thisStart;
        final int otherLen = substring.length() - start;

        // Check for invalid parameters
        if (thisStart < 0 || start < 0 || length < 0) {
            return false;
        }

        // Check that the regions are long enough
        if (srcLen < length || otherLen < length) {
            return false;
        }

        while (tmpLen-- > 0) {
            final char c1 = cs.charAt(index1++);
            final char c2 = substring.charAt(index2++);

            if (c1 == c2) {
                continue;
            }

            if (!ignoreCase) {
                return false;
            }

            // The real same check as in String.regionMatches():
            final char u1 = Character.toUpperCase(c1);
            final char u2 = Character.toUpperCase(c2);
            if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2)) {
                return false;
            }
        }

        return true;
    }
}
