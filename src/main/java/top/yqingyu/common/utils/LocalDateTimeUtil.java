package top.yqingyu.common.utils;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.utils.LocalDateTimeUtil
 * @description
 * @createTime 2022年09月15日 01:03:00
 */
public class LocalDateTimeUtil {

    public static final DateTimeFormatter HTTP_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z").withZone(ZoneId.ofOffset("GMT", ZoneOffset.UTC));



    public static String formatHttpTime(String str) {

        return str
                .replace("周一", "Mon")
                .replace("周二", "Tue")
                .replace("周三", "Wed")
                .replace("周四", "Thu")
                .replace("周五", "Fri")
                .replace("周六", "Sat")
                .replace("周日", "Sun")
                .replace("1月", "Jan")
                .replace("2月", "Feb")
                .replace("3月", "Mar")
                .replace("4月", "Apr")
                .replace("5月", "May")
                .replace("6月", "Jun")
                .replace("7月", "Jul")
                .replace("8月", "Aug")
                .replace("9月", "Sep")
                .replace("10月", "Oct")
                .replace("11月", "Nov")
                .replace("12月", "Dec");

    }
}
