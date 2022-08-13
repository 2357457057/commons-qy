package top.yqingyu.common.utils;


import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TimeUtil {

    // 实时
    public static String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        String timeStamp = year
                + "-"
                + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month))
                + "-"
                + (String.valueOf(day).length() == 1 ? ("0" + String
                .valueOf(day)) : String.valueOf(day))
                + " "
                + (String.valueOf(hour).length() == 1 ? ("0" + String
                .valueOf(hour)) : String.valueOf(hour))
                + ":"
                + (String.valueOf(minute).length() == 1 ? ("0" + String
                .valueOf(minute)) : String.valueOf(minute))
                + ":"
                + (String.valueOf(second).length() == 1 ? ("0" + String
                .valueOf(second)) : String.valueOf(second));
        return timeStamp;
    }

    // 实时 格式YYYYMMDDHHMISS
    public static String getRubCurrenttime() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        String timeStamp = year
                + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month))
                + (String.valueOf(day).length() == 1 ? ("0" + String
                .valueOf(day)) : String.valueOf(day))
                + (String.valueOf(hour).length() == 1 ? ("0" + String
                .valueOf(hour)) : String.valueOf(hour))
                + (String.valueOf(minute).length() == 1 ? ("0" + String
                .valueOf(minute)) : String.valueOf(minute))
                + (String.valueOf(second).length() == 1 ? ("0" + String
                .valueOf(second)) : String.valueOf(second));
        return timeStamp;
    }

    // 实时格式DD日HH时MI分
    public static String getCurrentDHM() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String timeStamp = String.valueOf(day) + "日"
                + String.valueOf(hour) + "时"
                + String.valueOf(minute) + "分";
        return timeStamp;
    }

    // 本月最后一天
    public static String getLastdayofmonth() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        String timeStamp = year
                + "-"
                + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month))
                + "-"
                + (String.valueOf(day).length() == 1 ? ("0" + String
                .valueOf(day)) : String.valueOf(day)) + " "
                + "23:59:59";
        return timeStamp;
    }

    // public 当天0时
    public static String getTodayzero() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        String timeStamp = year
                + "-"
                + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month))
                + "-"
                + (String.valueOf(day).length() == 1 ? ("0" + String
                .valueOf(day)) : String.valueOf(day)) + " "
                + "00:00:00";
        return timeStamp;
    }


    /**
     * 获取指定月份偏差的时间
     *
     * @param offMon
     * @return YYYY年MM月DD日
     */
    public static String getOffYMD(int offMon) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, offMon);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        String timeStamp = year
                + "年"
                + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month))
                + "月"
                + (String.valueOf(day).length() == 1 ? ("0" + String
                .valueOf(day)) : String.valueOf(day)) + "日";
        return timeStamp;
    }


    // 昨天23时
    public static String getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String timeStamp = year
                + "-"
                + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month))
                + "-"
                + (String.valueOf(day).length() == 1 ? ("0" + String
                .valueOf(day)) : String.valueOf(day)) + " "
                + "23:59:59";
        return timeStamp;
    }

    // 下月第一天
    public static String getNextmonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = 1;
        int year = cal.get(Calendar.YEAR);
        String timeStamp = year
                + "-"
                + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month))
                + "-"
                + (String.valueOf(day).length() == 1 ? ("0" + String
                .valueOf(day)) : String.valueOf(day)) + " "
                + "00:00:00";
        return timeStamp;
    }

    // 下月第一天
    public static String getNextmonthFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = 1;
        int year = cal.get(Calendar.YEAR);
        String timeStamp = year
                + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month))
                + (String.valueOf(day).length() == 1 ? ("0" + String
                .valueOf(day)) : String.valueOf(day));
        return timeStamp;
    }

    //获取下月yyyyMM格式
    public static String getNextYearMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String timeStamp = year
                + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month));
        return timeStamp;
    }


    public static String getNextYMD() {
        String nextDay = getNextYearMonth();
        return nextDay + "01";
    }


    /**
     * 获取指定月的下月
     * yyyy年MM月格式
     */
    public static String getAppointNextYM(String str) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(str.substring(0, 4)));
        cal.set(Calendar.MONTH, Integer.parseInt(str.substring(4, 6)));
//		cal.add(Calendar.MONTH, 1);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        String timeStamp = year + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month));
        return timeStamp;
    }

    //获取指定月的偏差月yyyyMM格式
    public static String getAppointOffYM(String str, String offMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(str.substring(0, 4)));
        cal.set(Calendar.MONTH, Integer.parseInt(str.substring(4, 6)) - 1);
        cal.add(Calendar.MONTH, Integer.parseInt(offMonth));
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        String timeStamp = year + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month));
        return timeStamp;
    }

    // 明天 格式：YYYY-MM-DD HH:MI:SS
    public static String getTomorrowTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        String timeStamp = year
                + "-"
                + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month))
                + "-"
                + (String.valueOf(day).length() == 1 ? ("0" + String
                .valueOf(day)) : String.valueOf(day)) + " "
                + "00:00:00";
        return timeStamp;
    }

    // 本月第一天
    public static String getFirstdayofmonth() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int day = 1;
        int year = cal.get(Calendar.YEAR);
        String timeStamp = year
                + "-"
                + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month))
                + "-"
                + (String.valueOf(day).length() == 1 ? ("0" + String
                .valueOf(day)) : String.valueOf(day)) + " "
                + "00:00:00";
        return timeStamp;
    }

    // 3个月以前
    public static String getThreemonthago() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -3);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        String timeStamp = year
                + "-"
                + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month))
                + "-"
                + (String.valueOf(day).length() == 1 ? ("0" + String
                .valueOf(day)) : String.valueOf(day))
                + " "
                + (String.valueOf(hour).length() == 1 ? ("0" + String
                .valueOf(hour)) : String.valueOf(hour))
                + ":"
                + (String.valueOf(minute).length() == 1 ? ("0" + String
                .valueOf(minute)) : String.valueOf(minute))
                + ":"
                + (String.valueOf(second).length() == 1 ? ("0" + String
                .valueOf(second)) : String.valueOf(second));
        return timeStamp;
    }

    public static String getMonthBeforeNow(int i) {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1 - i;
        int year = c.get(Calendar.YEAR);
        if (month <= 0) {
            year = c.get(Calendar.YEAR) - 1;
            month += 12;
        }
        return String.valueOf(year)
                + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month));
    }

    /**
     * 判断当前时间是否在指定时间范围内
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return boolean
     * @throws Exception
     */
    public static boolean judgeNowBetweenB2E(String beginTime, String endTime) throws Exception {
        boolean flag = false;
        String nowTime = getThisDate();
        Date d1 = string2Date(nowTime);
        Date bt = string2Date(beginTime);
        Date et = string2Date(endTime);
        if (d1.after(bt) && d1.before(et)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 判断当前时间是否在指定时间范围内（>=,<=）
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return boolean
     * @throws Exception
     */
    public static boolean isNowBetweenB2E(String format, String beginTime, String endTime) throws Exception {
        boolean flag = false;
        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        Date nowDate = calendar.getTime();
        Date beginDate = df.parse(beginTime);
        Date endDate = df.parse(endTime);
        String nowDateStr = df.format(nowDate);
        if ((nowDateStr.equals(beginTime) || nowDate.after(beginDate))
                && (nowDateStr.equals(endTime) || nowDate.before(endDate))) {
            flag = true;
        }
        return flag;
    }


    /**
     * 判断时间的先后
     *
     * @param firsttime
     * @param sectime
     * @param judgefox
     * @return
     */
    public static boolean judgeTime(Object firsttime, Object sectime,
                                    String judgefox) {
        boolean re = false;
        DateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String nowtime = "";
        String puttime = "";
        if (!(firsttime instanceof String))
            nowtime = dateFormat.format(firsttime);
        else
            nowtime = (String) firsttime;
        if (!(sectime instanceof String))
            puttime = dateFormat.format(sectime);
        else
            puttime = (String) sectime;
        if (nowtime.equalsIgnoreCase("now")) {
            nowtime = dateFormat.format(new Date());
        }
        if (puttime.equalsIgnoreCase("now")) {
            puttime = dateFormat.format(new Date());
        }
        int tj = nowtime.compareTo(puttime);
        if (tj > 0) {
            if (">".equals(judgefox) || ">=".equals(judgefox))
                re = true;
        } else if (tj == 0) {
            if ("=".equals(judgefox) || ">=".equals(judgefox)
                    || "<=".equals(judgefox))
                re = true;
        } else {
            if ("<".equals(judgefox) || "<=".equals(judgefox))
                re = true;
        }
        return re;
    }

    /**
     * month =0时为本月最后一天 month =1是为下个月最后一天 month =-1是为上个月最后一天
     */
    public static String getLastdayofmonth(int months) {
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MONTH, months);
        cal.set(Calendar.DATE, 1);//把日期设置为当月第一天    
        cal.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天    

        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DATE);
        String timeStamp = year
                + "-"
                + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month))
                + "-"
                + (String.valueOf(day).length() == 1 ? ("0" + String
                .valueOf(day)) : String.valueOf(day)) + " "
                + "23:59:59";
        return timeStamp;
    }


    /**
     * month =0时为本月第一天 month =1是为下个月第一天 month =-1是为上个月第一天
     */
    public static String getFirstdayofmonth(int months) {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1 + months;
        int day = 1;
        int year = cal.get(Calendar.YEAR);
        String timeStamp = year
                + "-"
                + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month))
                + "-"
                + (String.valueOf(day).length() == 1 ? ("0" + String
                .valueOf(day)) : String.valueOf(day)) + " "
                + "00:00:00";
        return timeStamp;
    }

    /**
     * 获取当前时间，时间格式：MM-DD
     *
     * @return
     */
    public static String getthisDate() {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("MMdd");
        Date d = new Date();
        String display = bartDateFormat.format(d);
        return display;
    }

    /**
     * 获取上个月份，时间格式: MM
     *
     * @return
     */
    public static String getlastMonth() {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM");
        Date d = new Date();
        int month = Integer.parseInt(bartDateFormat.format(d)) - 1;
        if (month == 0) {
            month = 12;
        }
        String display = String.valueOf(month);
        return display;
    }

    /**
     * 获取年
     */
    public static String getYearTime(String dateformat, int subtrahend) {
        Date now = new Date();

        // 日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
        String year = dateFormat.format(now);
        int yearValueInt = Integer.parseInt(year) - subtrahend;
        String yearValueStr = String.valueOf(yearValueInt);

        return yearValueStr;
    }

    /**
     * 获取月份，时间格式: yyyyMM
     *
     * @return
     */
    public static String getCurrealYearMonth() {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMM");
        Date d = new Date();
        String time = bartDateFormat.format(d);
        return time;
    }


    /**
     * 获取月份，时间格式: yyyyMM
     *
     * @return
     */
    public static String getCurrentYM() {
        String currentTime = getCurrealYearMonth();
        return currentTime.substring(0, 4) + "年" + currentTime.substring(4) + "月";
    }


    /**
     * 获取月份，时间格式: yyyyMMdd
     *
     * @return
     */
    public static String getCurrealYearMonthDayHH() {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHH");
        Date d = new Date();
        String time = bartDateFormat.format(d);
        return time;
    }

    /**
     * 获取上个月份，时间格式：yyyymm
     *
     * @return
     */
    public static String getpreviousYearMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        String dateStr = sdf.format(c.getTime());

        return dateStr;
    }

    /**
     * 判断当前时间是否为当月的第一天
     *
     * @return
     */
    public static boolean judgeThisMonthFirstDay() {
        String thisday = String.valueOf(Integer.parseInt(getthisDate()
                .substring(2, 4)));
        if (thisday.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 开始时长/开机的时长:yyyy-MM-dd HH-mm-ss
     *
     * @param hours
     * @return 格式化时间
     */
    public static String getDate(int hours) {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH-mm-ss ");
        Calendar calendar = Calendar.getInstance();
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.set(Calendar.HOUR_OF_DAY, nowHour + hours);
        Date d = calendar.getTime();
        String display = bartDateFormat.format(d).toString();
        return display;
    }

    /**
     * 根据指定参数获取时间 格式：yyyyMM
     *
     * @param index
     * @return
     */
    public static String getAccountDate(int index) {
        String date = getLastMonth(index - 1);
        return date;
    }

    /**
     * 根据指定参数获取年月 yyyyMM
     *
     * @param i
     * @return
     */
    public static String getLastMonth(int i) {
        String mo = "";
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) - i;
        int year = c.get(Calendar.YEAR);
        if (month <= 0) {
            year = c.get(Calendar.YEAR) - 1;
            month += 12;
        }
        mo = String.valueOf(month);
        if (mo.length() == 1) {
            mo = "0" + mo;
        }
        return String.valueOf(year) + mo;
    }

    /**
     * 根据指定参数获取当前月
     */
    public static String getthismonth() {

        SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM");
        Date d = new Date();
        String date = bartDateFormat.format(d);
        return date;
    }

    /**
     * 获取当前年月日
     */
    public static String getSysdate() {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        String date = bartDateFormat.format(d);
        return date;
    }

    /**
     * 获取指定当前时间 格式：yyyy-mm-dd
     *
     * @return
     */
    public static String getAccountDate() {
        String mo = "";
        String d = "";
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_MONTH);
        d = String.valueOf(day);
        mo = String.valueOf(month);
        if (mo.length() == 1) {
            mo = "0" + mo;
        }
        if (d.length() == 1) {
            d = "0" + d;
        }
        return String.valueOf(year) + "-" + mo + "-" + d;
    }

    /**
     * 获取指定当前时间 格式：yyyy年mm月dd日
     *
     * @return
     */
    public static String getCurrentDate() {
        String mo = "";
        String d = "";
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_MONTH);
        d = String.valueOf(day);
        mo = String.valueOf(month);
        if (mo.length() == 1) {
            mo = "0" + mo;
        }
        if (d.length() == 1) {
            d = "0" + d;
        }
        return String.valueOf(year) + "年" + mo + "月" + d + "日";
    }

    /**
     * 获取指定offDay后的时间 格式：yyyy年mm月dd日
     * input:offDay（偏量时间）
     *
     * @return:yyyy年mm月dd日
     */
    public static String getAppointDateYMD(int offDay) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, offDay);

        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        String mo = "";
        String d = "";
        d = String.valueOf(day);
        mo = String.valueOf(month);
        if (mo.length() == 1) {
            mo = "0" + mo;
        }
        if (d.length() == 1) {
            d = "0" + d;
        }
        return String.valueOf(year) + "年" + mo + "月" + d + "日";
    }


    /**
     * 获取指定offDay后的时间 格式：yyyymmdd
     * input:offDay（偏量时间）
     *
     * @return:yyyymmdd
     */
    public static String getAppDateYYYYMMDD(int offDay) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, offDay);

        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        String mo = "";
        String d = "";
        d = String.valueOf(day);
        mo = String.valueOf(month);
        if (mo.length() == 1) {
            mo = "0" + mo;
        }
        if (d.length() == 1) {
            d = "0" + d;
        }
        return String.valueOf(year) + mo + d;
    }


    /**
     * 获取指定当前时间 格式：yyyy
     *
     * @return
     */
    public static int getThisYear() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        return year;
    }

    /**
     * 获取指定当前时间 格式：yyyymmdd
     *
     * @return
     */
    public static String getThisDate() {
        String mo = "";
        String d = "";
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_MONTH);
        d = String.valueOf(day);
        mo = String.valueOf(month);
        if (mo.length() == 1) {
            mo = "0" + mo;
        }
        if (d.length() == 1) {
            d = "0" + d;
        }
        return String.valueOf(year) + mo + d;
    }


    /**
     * 获取当前季度：20XX年第X季度
     *
     * @return
     */
    public static String getCurrentSession() {
        String mo = "";
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        switch (month) {
            case 1:
            case 2:
            case 3:
                mo = "一";
                break;
            case 4:
            case 5:
            case 6:
                mo = "二";
                break;
            case 7:
            case 8:
            case 9:
                mo = "三";
                break;
            case 10:
            case 11:
            case 12:
                mo = "四";
                break;
        }
        return String.valueOf(year) + "年第" + mo + "季度";
    }

    /**
     * 获取当月一号 格式：yyyy-mm-dd
     *
     * @return
     */
    public static String getFirstDayForThisMonth() {
        String mo = "";
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        mo = String.valueOf(month);
        if (mo.length() == 1) {
            mo = "0" + mo;
        }
        return String.valueOf(year) + "-" + mo + "-01";
    }

    /**
     * 获取当月一号 格式：yyyymmdd
     *
     * @return
     */
    public static String getFirstDayOfThisMonth() {
        String thisFirstDay = getFirstDayForThisMonth();
        return thisFirstDay.replace("-", "");
    }

    /**
     * 获取当月最后一天 格式：yyyymmdd
     *
     * @return
     */
    public static String getLastDayOfThisMonth() {
        String thisLastDay = getLastdayofmonth();
        return thisLastDay.replace("-", "").substring(0, 8);
    }

    // 第二天23时59分59秒   格式：YYYY-MM-DD HH:MI:SS
    public static String getTomorrowDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        String timeStamp = year
                + "-"
                + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month))
                + "-"
                + (String.valueOf(day).length() == 1 ? ("0" + String
                .valueOf(day)) : String.valueOf(day)) + " "
                + "23:59:59";
        return timeStamp;
    }


    /**
     * 获取上月最后一天 格式：yyyyMMdd
     *
     * @return 上月最后一天
     * @auther jianghj2
     */
    public static String getLastDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1); //
        Date theDate = calendar.getTime();
        String s = df.format(theDate);
        return s;
    }


    /**
     * 获取上一个小时的时间 格式：yyyy-MM-dd HH:mm:ss
     *
     * @return 上一个小时
     * @auther jianghj2
     */
    public static String getLastHour() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -1);
        Date theDate = calendar.getTime();
        String s = df.format(theDate);
        return s;
    }

    /**
     * 获取上一个小时的时间 格式：yyyyMMdd
     *
     * @return 上一个小时
     * @auther jianghj2
     */
    public static String getFstdayofmonth() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int day = 1;
        int year = cal.get(Calendar.YEAR);
        String timeStamp = year

                + (String.valueOf(month).length() == 1 ? ("0" + String
                .valueOf(month)) : String.valueOf(month))

                + (String.valueOf(day).length() == 1 ? ("0" + String
                .valueOf(day)) : String.valueOf(day));
        return timeStamp;
    }

    /**
     * 把时间串按照响应的格式转换成日期对象
     *
     * @param dateStr 时间串
     * @param format  指定的格式
     * @return 返回java.util.Date的对象, 转换失败时返回当前的时间对象
     */
    public static Date string2Date(String dateStr, String format) throws Exception {
        if (dateStr == null || format == null) {
            {
                throw new Exception("日期转换失败:dateStr or format is null");
            }
        }
        try {
            SimpleDateFormat dateFormater = new SimpleDateFormat(format);
            return dateFormater.parse(dateStr);
        } catch (Exception e) {
            throw e;
        }
    }

    public static Date string2Date(String str) throws Exception {
        if (str == null)
            return null;
        String format = null;

        if (str.length() == 19) {
            format = "yyyy-MM-dd HH:mm:ss";
        } else if (str.length() == 14) {
            format = "yyyyMMddHHmmss";
        } else if (str.length() == 12) {
            format = "yyyyMMddHHmm";
        } else if (str.length() == 10) {
            format = "yyyyMMddHH";
        } else if (str.length() == 8) {
            format = "yyyyMMdd";
        } else if (str.length() == 6) {
            format = "yyyyMM";
        } else if (str.length() == 4) {
            format = "yyyy";
        } else {
            throw new Exception("时间格式错误:" + str);
        }

        return string2Date(str, format);
    }

    /**
     * 判断传入月份是否为最近六个月 robin
     *
     * @param month
     * @return yyyyMM
     */
    public static String getLastSixMonth(String month) {
        String curMonth = TimeUtil.getthismonth();
        int intCurMon = Integer.parseInt(curMonth);//当前月份
        int paraMon = 0;
        try {
            paraMon = Integer.parseInt(month);//传入月份
            if (paraMon > 12) {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
        int year = TimeUtil.getThisYear();
        if (((paraMon > intCurMon && ((intCurMon + 12) - paraMon < 6))) ||
                ((intCurMon >= paraMon && intCurMon - paraMon < 6))) {

            if ((paraMon > intCurMon && ((intCurMon + 12) - paraMon < 6))) {
                year = TimeUtil.getThisYear() - 1;
            }
            if (month.length() == 1) {
                month = "0" + month;
            }
            return year + month;
        } else {
            return "";
        }
    }

    /**
     * 日期格式转换年月提示信息
     *
     * @param strMonth 201107
     * @return 2011年7月
     */
    public static String getYearMonthString(String strMonth) {
        String strYear = strMonth.substring(0, 4);
        String month = strMonth.substring(4);
        int iMonth = Integer.parseInt(month);
        return strYear + "年" + iMonth + "月";
    }

    /**
     * 判断网龄
     *
     * @param strDate
     * @return
     */
    public static int judgeNetAge(String strDate) {
        Date dt = new Date();
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        String cycle = matter1.format(dt);
        Date start = null;                              // 当前时间
        Date end = null;    // 开户时间 
        try {
            start = matter1.parse(cycle);
            end = matter1.parse(strDate);
        } catch (Exception e) {
            return 0;
        }
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);

        int year = endCalendar.get(Calendar.YEAR)
                - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH)
                - startCalendar.get(Calendar.MONTH);

        if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }

    /**
     * 获得两个日期之前相差的月份<br>
     *
     * @param start : 开始月份
     * @param end   : 结束月份
     * @return
     */
    public static int GetSubMonth(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);

        int year = endCalendar.get(Calendar.YEAR)
                - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH)
                - startCalendar.get(Calendar.MONTH);

        if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }

    /**
     * 判断日期格式是否正确
     *
     * @param format
     * @return
     */
    public static boolean isDateFormat(String date, String format) {
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(date);
        boolean flag = matcher.matches();
        return flag;
    }

    /**
     * 根据传入日期格式获取系统当前时间
     *
     * @param format
     * @return
     */
    public static String getDateFormatStr(String format) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String DateStr = sdf.format(new Date());
        return DateStr;
    }

    /**
     * 判断传入的时间是否在指定的前几个月之内
     *
     * @param date
     * @param month
     * @return
     */
    public static boolean isInFiveMonth(String date, int month) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar afterCalendar = Calendar.getInstance();
        Calendar beforeCalendar = Calendar.getInstance();
        afterCalendar.add(Calendar.MONTH, -1);
        beforeCalendar.add(Calendar.MONTH, -month);
        Date afterDate = afterCalendar.getTime();
        Date beforeDate = beforeCalendar.getTime();
        afterDate = sdf.parse(sdf.format(afterDate));
        beforeDate = sdf.parse(sdf.format(beforeDate));
        Date time = sdf.parse(date);

        if (time.before(beforeDate) || time.after(afterDate)) {
            return false;
        }

        return true;
    }

    /**
     * 判断传入的月份值是否在前5个月内
     *
     * @param month
     * @return
     */
    public static boolean isInFiveMonth(String month) throws Exception {
        if (StringUtils.isNotEmpty(month) && StringUtils.isNumeric(month)) {
            int m = Integer.parseInt(month);
            int monthNumber;
            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < 5; i++) {
                calendar.add(Calendar.MONTH, -1);
                monthNumber = calendar.get(Calendar.MONTH) + 1;

                if (monthNumber == m) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断传入的月份值是否在前n个月内
     *
     * @param month
     * @return
     */
    public static boolean isInFiveMonth(int n, String month) throws Exception {
        if (StringUtils.isNotEmpty(month) && StringUtils.isNumeric(month)) {
            int m = Integer.parseInt(month);
            int monthNumber;
            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < n; i++) {
                calendar.add(Calendar.MONTH, -1);
                monthNumber = calendar.get(Calendar.MONTH) + 1;
                if (monthNumber == m) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 获取传入的月份值是前第几个月
     *
     * @param month
     * @return
     */
    public static int getBeforeMonthNum(String month) throws Exception {
        if (StringUtils.isNotEmpty(month) && StringUtils.isNumeric(month)) {
            int m = Integer.parseInt(month);
            int monthNumber;
            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < 12; i++) {
                calendar.add(Calendar.MONTH, -1);
                monthNumber = calendar.get(Calendar.MONTH) + 1;
                if (monthNumber == m) {
                    return i + 1;
                }
            }
        }
        return 0;
    }

    /**
     * 根据传入的月份值获取时间(yyyyMM)
     *
     * @param month
     * @return
     */
    public static String getFormatMonth(String month) throws Exception {
        String yStr = "";
        String mStr = "";

        if (StringUtils.isNotEmpty(month) && StringUtils.isNumeric(month)) {
            int mm = Integer.parseInt(month);
            Calendar calendar = Calendar.getInstance();
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH) + 1;

            if (m < mm) {
                y = (y - 1);
                yStr = String.valueOf(y);
            } else {
                yStr = String.valueOf(y);
            }

            if (mm < 10) {
                mStr = "0" + mm;
            } else {
                mStr = String.valueOf(mm);
            }
        }
        return yStr + mStr;
    }

    /**
     * 返回yyyyMM01
     *
     * @param date
     * @return
     */
    public static String getFirstdayofmonth(String date) throws Exception {
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = "01";

        return year + month + day;
    }

    /**
     * 返回yyyyMM30|31|28|29
     *
     * @param date
     * @return
     */
    public static String getLastdayofmonth(String date) throws Exception {
        String timeStamp = "";
        if (StringUtils.isNotEmpty(date) && date.length() >= 6) {
            String format = "^\\d{4}(0[1-9]|1[0-2])$"; // yyyyMM
            date = date.substring(0, 6);

            if (isDateFormat(date, format)) {
                int year = Integer.parseInt(date.substring(0, 4));
                int month = Integer.parseInt(date.substring(4, 6));
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month - 1);
                int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                timeStamp = year
                        + (String.valueOf(month).length() == 1 ? ("0" + String
                        .valueOf(month)) : String.valueOf(month))
                        + (String.valueOf(day).length() == 1 ? ("0" + String
                        .valueOf(day)) : String.valueOf(day));
            }

        }


        return timeStamp;
    }

    /**
     * 获取当前时间，时间格式：DD
     *
     * @return
     */
    public static int getThisDay() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("dd");
        Date date = new Date();
        String display = df.format(date);
        Integer i = Integer.parseInt(display);
        return i;
    }

    /**
     * 获取指定时间的前一个月时间
     *
     * @return
     */
    public static String getBeforeMonthTime(String format, String dateStr) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = df.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        return df.format(calendar.getTime());
    }

    /**
     * 获取指定时间的下一个月时间
     *
     * @return
     */
    public static String getNextMonthTime(String format, String dateStr) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = df.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        return df.format(calendar.getTime());
    }

    /**
     * 获取指定时间的前n个月时间
     *
     * @return
     */
    public static String getBeforeMonthTime(String format, String dateStr, int num) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = df.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - num);
        return df.format(calendar.getTime());
    }

    /**
     * 获取指定时间的前n天时间
     *
     * @return
     */
    public static String getBeforeDayTime(String format, String dateStr, int n) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = df.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - n);
        return df.format(calendar.getTime());
    }

    /**
     * 获取指定时间的后n天时间
     *
     * @return
     */
    public static String getAfterDayTime(String format, String dateStr, int n) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = df.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + n);
        return df.format(calendar.getTime());
    }

    /**
     * 判断data1是否在date2之后
     *
     * @return
     */
    public static Boolean isAfterDayTime(String format, String date1, String date2) throws Exception {
        boolean flag = false;
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date d1 = df.parse(date1);
        Date d2 = df.parse(date2);

        if (d1.after(d2)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 判断data1是否在date2之前
     *
     * @return
     */
    public static Boolean isBeforeDayTime(String format, String date1, String date2) throws Exception {
        boolean flag = false;
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date d1 = df.parse(date1);
        Date d2 = df.parse(date2);

        if (d1.before(d2)) {
            flag = true;
        }
        return flag;
    }

    public static String getZHCurrenttime() throws Exception {
        String time = getRubCurrenttime();   // 格式YYYYMMDDHHMISS
        StringBuffer formatTime = new StringBuffer();

        if (StringUtils.isNotEmpty(time) && time.length() == 14) {
            formatTime.append(time.substring(0, 4));
            formatTime.append("年");
            formatTime.append(time.substring(4, 6));
            formatTime.append("月");
            formatTime.append(time.substring(6, 8));
            formatTime.append("日");
            formatTime.append(time.substring(8, 10));
            formatTime.append("时");
            formatTime.append(time.substring(10, 12));
            formatTime.append("分");
        } else {
            throw new Exception("获取时间出错！" + "---getRubCurrenttime()");
        }

        return formatTime.toString();
    }

    /**
     * 获取当前时间下一天时间
     *
     * @param time
     */
    public static String getNextDay(String time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        Date formatDate = null;
        try {
            formatDate = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(formatDate);
        calendar.add(Calendar.DATE, 1);
        Date date = calendar.getTime();
        String s = df.format(date);
        return s;
    }

    /**
     * 获取当前时间下一天时间
     *
     * @param time
     */
    public static String getNextTime(String time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        Date formatDate = null;
        try {
            formatDate = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(formatDate);
        calendar.add(Calendar.DATE, 1);
        Date date = calendar.getTime();
        String s = df.format(date);
        return s;
    }

    /**
     * 获取当前时间下一年的今天
     *
     * @param time
     */
    public static String getNextYear(String time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Date formatDate = new Date();
        Date date = null;
        try {
            date = df.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, 1);
        String s = df.format(cal.getTime());
        return s;
    }

    /**
     * 获取指定年份2月份最后一天
     *
     * @param time
     */
    public static String getFebruaryLastDay(String time) {
        int year = Integer.parseInt(time);
        Calendar c = Calendar.getInstance();
        c.set(year, 2, 1); // 其实是这一年的3月1日
        // 把时间往前推一天，就是2月的最后一天
        c.add(Calendar.DATE, -1);
        int date = c.get(Calendar.DATE);
        return String.valueOf(date);
    }

    /**
     * 校验日期格式
     *
     * @param time
     */
    public static boolean isDateFormat(String time) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        try {
            sd.setLenient(false);//此处指定日期/时间解析是否不严格，在true是不严格，false时为严格
            sd.parse(time);//从给定字符串的开始解析文本，以生成一个日期
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getDistanceOfTwoDate(String before, String after) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date before1;
        long beforeTime = 0;
        long afterTime = 0;
        try {
            before1 = df.parse(before);
            Date after1 = df.parse(after);
            beforeTime = before1.getTime();
            afterTime = after1.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * 转换成Date时间格式
     *
     * @param time
     */
    public static Date getParseDate(String time) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = sd.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 判断网龄
     *
     * @param strDate
     * @return
     * @throws Exception
     */
    public static double judgeNetAge_New(String strDate) throws Exception {
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        double day = 0;
        double YEAR = 0;
        Date start = matter1.parse(strDate);
        Date end = new Date();
        day = (end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000);
        String year = StringUtil.formatDecimal(String.valueOf(day), "365");
        BigDecimal bd = new BigDecimal(String.valueOf(year)).setScale(1, RoundingMode.HALF_UP);
        YEAR = Double.parseDouble(String.valueOf(bd));
        return YEAR;
    }

    /**
     * 获取指定时间的月的最后一天
     *
     * @param repeatDate
     * @return
     */
    public static String getMaxMonthDate(String repeatDate, int num) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        try {
            if (StringUtils.isNotBlank(repeatDate) && !"null".equals(repeatDate)) {
                calendar.setTime(dft.parse(repeatDate));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.MONTH, num);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dft.format(calendar.getTime());
    }

    /**
     * 将时间格式化成指定格式
     *
     * @param
     * @param format 格式化字符
     * @return 日期格式化字符串
     */
    public static String dateToFormatString(String format) {
        Date date = null;
        if (StringUtils.isEmpty(format))
            format = "yyyy-MM-dd HH:mm:ss";
        date = new Date();
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 将时间格式化成指定格式
     *
     * @param date   日期
     * @param format 格式化字符
     * @return 日期格式化字符串
     */
    public static String dateToFormatString(Date date, String format) {
        if (StringUtils.isEmpty(format))
            format = "yyyy-MM-dd HH:mm:ss";
        if (date == null)
            date = new Date();
        return new SimpleDateFormat(format).format(date);
    }
}