package top.yqingyu.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XssUtil {
    // 正则表达式，匹配尖括号
    private static final Pattern pattern = Pattern.compile("[<>]");

    /**
     * 过滤XSS特殊字符，将尖括号转换为HTML实体
     *
     * @param input 待过滤的字符串
     * @return 过滤后的字符串
     */
    public static String filterXSS(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        Matcher matcher = pattern.matcher(input);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            if (matcher.group().equals("<")) {
                matcher.appendReplacement(sb, "&lt;");
            } else if (matcher.group().equals(">")) {
                matcher.appendReplacement(sb, "&gt;");
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}