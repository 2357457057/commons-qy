package top.yqingyu.common.qydata;

import com.alibaba.fastjson2.JSON;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.IData.DataHelper
 * @Description 偷来 并改为FastJSON2
 * @createTime 2022年07月06日 18:13:00
 */
public class DataHelper {

    public DataHelper() {
    }

    public static List strToList(String value) {
        if (value == null) {
            return null;
        } else {
            Pattern pattern = Pattern.compile("([\\[|\\{]+\")");
            Matcher matcher = pattern.matcher(value);
            if (!matcher.find() || !value.startsWith(matcher.group())) {
                StringBuffer str = new StringBuffer();
                pattern = Pattern.compile("(\\r)|(\\n)|(\\$)|(\", \")|(\\[?\"\\]?)");
                matcher = pattern.matcher(value);

                String group;
                while(matcher.find()) {
                    group = matcher.group();
                    if ("\r".equals(group)) {
                        matcher.appendReplacement(str, "!~5~!");
                    } else if ("\n".equals(group)) {
                        matcher.appendReplacement(str, "!~6~!");
                    } else if ("$".equals(group)) {
                        matcher.appendReplacement(str, "!~7~!");
                    } else if ("\"".equals(group)) {
                        matcher.appendReplacement(str, "!~8~!");
                    }
                }

                matcher.appendTail(str);
                value = str.toString();
                str = new StringBuffer();
                pattern = Pattern.compile("(\".*?\")");
                matcher = pattern.matcher(value);

                while(matcher.find()) {
                    group = matcher.group();
                    StringBuffer substr = new StringBuffer();
                    Pattern subpattern = Pattern.compile("(\\{)|(\\[)|(\\])|(,)");
                    Matcher submatcher = subpattern.matcher(group);

                    while(submatcher.find()) {
                        String subgroup = submatcher.group();
                        if ("{".equals(subgroup)) {
                            submatcher.appendReplacement(substr, "!~1~!");
                        } else if ("[".equals(subgroup)) {
                            submatcher.appendReplacement(substr, "!~2~!");
                        } else if ("]".equals(subgroup)) {
                            submatcher.appendReplacement(substr, "!~3~!");
                        } else if (",".equals(subgroup)) {
                            submatcher.appendReplacement(substr, "!~4~!");
                        }
                    }

                    submatcher.appendTail(substr);
                    matcher.appendReplacement(str, substr.toString());
                }

                matcher.appendTail(str);
                value = str.toString();
                str = new StringBuffer();
                pattern = Pattern.compile("(=?[\\{\\[][\\{\\}\\[\\]]*(, )?[\\{\\}\\[]*)|([\\}\\]]*, [\\{\\[]*)|(\", \")");
                matcher = pattern.matcher(value);

                while(matcher.find()) {
                    group = matcher.group();
                    if (group.startsWith("=")) {
                        group = "\":" + group.substring(1);
                    }

                    if (group.endsWith("{")) {
                        group = group + "\"";
                    } else if (group.endsWith(" ")) {
                        group = group + "\"";
                    }

                    group = group.replaceFirst(" ", "");
                    matcher.appendReplacement(str, group);
                }

                matcher.appendTail(str);
                value = str.toString();
                str = new StringBuffer();
                pattern = Pattern.compile("(:\\[\".*?\"\\])");
                matcher = pattern.matcher(value);

                while(matcher.find()) {
                    group = matcher.group();
                    if (Pattern.compile("(\",\")").matcher(group).find()) {
                        matcher.appendReplacement(str, group);
                    } else {
                        matcher.appendReplacement(str, group.substring(0, 1) + group.substring(2, group.length() - 1));
                    }
                }

                matcher.appendTail(str);
                value = str.toString();
                str = new StringBuffer();
                pattern = Pattern.compile("(!~1~!)|(!~2~!)|(!~3~!)|(!~4~!)|(!~5~!)|(!~6~!)|(!~7~!)|(!~8~!)");
                matcher = pattern.matcher(value);

                while(matcher.find()) {
                    group = matcher.group();
                    if ("!~1~!".equals(group)) {
                        matcher.appendReplacement(str, "{");
                    } else if ("!~2~!".equals(group)) {
                        matcher.appendReplacement(str, "[");
                    } else if ("!~3~!".equals(group)) {
                        matcher.appendReplacement(str, "]");
                    } else if ("!~4~!".equals(group)) {
                        matcher.appendReplacement(str, ",");
                    } else if ("!~5~!".equals(group)) {
                        matcher.appendReplacement(str, "\\\\r");
                    } else if ("!~6~!".equals(group)) {
                        matcher.appendReplacement(str, "\\\\n");
                    } else if ("!~7~!".equals(group)) {
                        matcher.appendReplacement(str, "\\$");
                    } else if ("!~8~!".equals(group)) {
                        matcher.appendReplacement(str, "\\\\\"");
                    }
                }

                matcher.appendTail(str);
                value = str.toString();
            }

            if (!value.startsWith("[") || !value.endsWith("]")) {
                value = "[" + value + "]";
            }

           return JSON.parseObject(value,DatasetList.class);
//            return (List) JSONSerializer.toJSON(value);

        }
    }
}
