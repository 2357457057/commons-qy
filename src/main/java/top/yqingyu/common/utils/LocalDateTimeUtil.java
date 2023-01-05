package top.yqingyu.common.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.utils.LocalDateTimeUtil
 * @description
 * @createTime 2022年09月15日 01:03:00
 */
public class LocalDateTimeUtil {

    public static final DateTimeFormatter HTTP_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z").withZone(ZoneId.ofOffset("GMT", ZoneOffset.UTC));
    public static final DateTimeFormatter DEFAULT_FORMATTER;

    public static final String YMD_STD = "yyyy-MM-dd";
    public static final String YMD_NO_SYMBOL = "yyyyMMdd";
    public static final String HM24S_STD = "HH:mm:ss";
    public static final String HM24S_NO_SYMBOL = "HHmmss";
    public static final String HM24SMS_STD = "HH:mm:ss.SSS";
    public static final String HM24SMS_NO_SYMBOL = "HHmmssSSS";
    public static final String STD = "yyyy-MM-dd HH:mm:ss";
    public static final String STD_NO_SYMBOL = "yyyyMMddHHmmss";
    public static final String STD_NO_SYMBOL2 = "yyyyMMdd HHmmss";
    public static final String FULL = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String FULL_NO_SYMBOL = "yyyyMMddHHmmssSSS";
    public static final String FULL_NO_SYMBOL2 = "yyyyMMdd HHmmssSSS";

    public static final ZoneId CHN = ZoneOffset.ofHours(8);
    public static final ZoneId UTC = ZoneOffset.UTC;

    public static final Map<String, DateTimeFormatter> DFT_FORMATTER_MAP = new IdentityHashMap<>(12);
    public static final Map<String, DateTimeFormatter> UTC_FORMATTER_MAP = new IdentityHashMap<>(12);

    static {
        DFT_FORMATTER_MAP.put(YMD_STD, getFormatter(YMD_STD));
        DFT_FORMATTER_MAP.put(YMD_NO_SYMBOL, getFormatter(YMD_NO_SYMBOL));
        DFT_FORMATTER_MAP.put(HM24S_STD, getFormatter(HM24S_STD));
        DFT_FORMATTER_MAP.put(HM24S_NO_SYMBOL, getFormatter(HM24S_NO_SYMBOL));
        DFT_FORMATTER_MAP.put(HM24SMS_STD, getFormatter(HM24SMS_STD));
        DFT_FORMATTER_MAP.put(HM24SMS_NO_SYMBOL, getFormatter(HM24SMS_NO_SYMBOL));
        DFT_FORMATTER_MAP.put(STD, getFormatter(STD));
        DFT_FORMATTER_MAP.put(STD_NO_SYMBOL, getFormatter(STD_NO_SYMBOL));
        DFT_FORMATTER_MAP.put(STD_NO_SYMBOL2, getFormatter(STD_NO_SYMBOL2));
        DFT_FORMATTER_MAP.put(FULL, getFormatter(FULL));
        DFT_FORMATTER_MAP.put(FULL_NO_SYMBOL, getFormatter(FULL_NO_SYMBOL));
        DFT_FORMATTER_MAP.put(FULL_NO_SYMBOL2, getFormatter(FULL_NO_SYMBOL2));

        UTC_FORMATTER_MAP.put(YMD_STD, getFormatter(YMD_STD, UTC));
        UTC_FORMATTER_MAP.put(YMD_NO_SYMBOL, getFormatter(YMD_NO_SYMBOL, UTC));
        UTC_FORMATTER_MAP.put(HM24S_STD, getFormatter(HM24S_STD, UTC));
        UTC_FORMATTER_MAP.put(HM24S_NO_SYMBOL, getFormatter(HM24S_NO_SYMBOL, UTC));
        UTC_FORMATTER_MAP.put(HM24SMS_STD, getFormatter(HM24SMS_STD, UTC));
        UTC_FORMATTER_MAP.put(HM24SMS_NO_SYMBOL, getFormatter(HM24SMS_NO_SYMBOL, UTC));
        UTC_FORMATTER_MAP.put(STD, getFormatter(STD, UTC));
        UTC_FORMATTER_MAP.put(STD_NO_SYMBOL, getFormatter(STD_NO_SYMBOL, UTC));
        UTC_FORMATTER_MAP.put(STD_NO_SYMBOL2, getFormatter(STD_NO_SYMBOL2, UTC));
        UTC_FORMATTER_MAP.put(FULL, getFormatter(FULL, UTC));
        UTC_FORMATTER_MAP.put(FULL_NO_SYMBOL, getFormatter(FULL_NO_SYMBOL, UTC));
        UTC_FORMATTER_MAP.put(FULL_NO_SYMBOL2, getFormatter(FULL_NO_SYMBOL2, UTC));

        DEFAULT_FORMATTER = new DateTimeFormatterBuilder()
                .appendPattern("[yyyy[-MM][-dd]][yyyyMMdd][yyyyMM][' 'HHmmssSSS][' 'HHmmss][yyyyMMddHHmmss][yyyyMMddHHmmssSSS][[' ']HH[:mm][:ss][.SSS]]")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
                .toFormatter();
    }

    public static DateTimeFormatter getFormatter(String formatStr, ZoneId zoneId) {
        return DateTimeFormatter.ofPattern(formatStr).withZone(zoneId);
    }

    public static DateTimeFormatter getFormatter(String formatStr) {
        return getFormatter(formatStr, CHN);
    }

    public static LocalDateTime of(Date date) {
        if (null == date) {
            return null;
        }
        if (date instanceof DateTime) {
            return of(date.toInstant(), ((DateTime) date).getZoneId());
        }
        return of(date.toInstant());
    }

    public static LocalDateTime of(TemporalAccessor temporalAccessor) {
        if (null == temporalAccessor)
            return null;
        return temporalAccessor instanceof LocalDate ?
                ((LocalDate) temporalAccessor).atStartOfDay() :
                LocalDateTime.of(
                        getByUnit(temporalAccessor, ChronoField.YEAR),
                        getByUnit(temporalAccessor, ChronoField.MONTH_OF_YEAR),
                        getByUnit(temporalAccessor, ChronoField.DAY_OF_MONTH),
                        getByUnit(temporalAccessor, ChronoField.HOUR_OF_DAY),
                        getByUnit(temporalAccessor, ChronoField.MINUTE_OF_HOUR),
                        getByUnit(temporalAccessor, ChronoField.SECOND_OF_MINUTE),
                        getByUnit(temporalAccessor, ChronoField.NANO_OF_SECOND));
    }

    public static LocalDateTime of(Instant instant) {
        return of(instant, CHN);
    }

    public static LocalDateTime of(Instant instant, ZoneId zoneId) {
        if (null == instant) {
            return null;
        }

        return LocalDateTime.ofInstant(instant, ObjectUtil.defaultIfNull(zoneId, ZoneId::systemDefault));
    }

    public static String format(String formatStr, LocalDateTime localDateTime) {
        if (DFT_FORMATTER_MAP.containsKey(formatStr)) {
            return DFT_FORMATTER_MAP.get(formatStr).format(localDateTime);
        }
        return getFormatter(formatStr).format(localDateTime);
    }

    public static String nowStr() {
        return DFT_FORMATTER_MAP.get(STD).format(LocalDateTime.now(CHN));
    }

    public static String nowStr(String format) {
        return format(format, LocalDateTime.now(CHN));
    }

    public static LocalDateTime parse(CharSequence str, DateTimeFormatter formatter) {
        if (null == str) return null;
        return null == formatter ? LocalDateTime.parse(str) : of(formatter.parse(str));
    }

    public static LocalDateTime parse(CharSequence str, String format) {
        if (null == str) return null;
        return parse(str, getFormatter(format));
    }

    /**
     * Default supposed parse
     * yyyy
     * yyyyMM
     * yyyy-MM
     * yyyyMMdd
     * yyyy-MM-dd
     * yyyyMMddHHmmss
     * yyyyMMddHHmmssSSS
     * yyyyMMdd HHmmss
     * yyyyMMdd HHmmssSSS
     * yyyy-MM-dd HH:mm:ss
     * yyyy-MM-dd HH:mm:ss.SSS
     * ====================================
     * <p>
     * Attention!!!!!!!!!!!
     * You can't just fill in time when parsing
     * If you find that you can still parse in addition to the above formats, congratulations!
     *
     * @description
     */
    public static LocalDateTime parse(CharSequence str) {
        if (null == str) return null;
        return of(DEFAULT_FORMATTER.parse(str));
    }

    public static long between(LocalDateTime start, LocalDateTime end, ChronoUnit unit) {
        return unit.between(start, end);
    }

    public static long between(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.MILLIS.between(start, end);
    }

    public static LocalDateTime startOfDay(LocalDateTime dateTime) {
        return dateTime.with(LocalTime.of(0, 0, 0, 0));
    }

    public static LocalDateTime endOfDay(LocalDateTime dateTime) {
        return dateTime.with(LocalTime.of(23, 59, 59, (int) ChronoField.NANO_OF_SECOND.range().getMaximum()));
    }

    /**
     * 提取时间单位
     *
     * @description
     */
    public static int getByUnit(TemporalAccessor temporalAccessor, TemporalField unit) {
        return temporalAccessor.isSupported(unit) ? temporalAccessor.get(unit) : (int) unit.range().getMinimum();
    }
}
