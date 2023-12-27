package top.yqingyu.common.utils;

import com.alibaba.fastjson2.JSONObject;
import top.yqingyu.common.bean.HuangLi;
import top.yqingyu.common.bean.LunarData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static top.yqingyu.common.utils.CCConstants.*;

public class ChinaCalendar {
    public static void main(String[] args) throws InterruptedException {
        LocalDate now = LocalDate.of(2023,11,1);
        for (int i = 0; i < 90; i++) {
            HuangLi instance = HuangLi.getInstance(now.plusDays(i));
            System.out.println(LocalDateTimeUtil.format(LocalDateTimeUtil.YMD_STD, LocalDateTime.of(now.plusDays(i), LocalTime.now()) ) + instance.getJieQi());
        }
    }

    /**
     * 干支纪年
     */
    public static int[] getGZYear(LocalDate localDate) {
        int sub = localDate.getYear() - CCConstants.JZ_YEAR.getYear();
        if (sub > 0) {
            sub = sub % 60;
        } else {
            sub = sub % 60 + 60;
        }
        int tg = sub % 10;
        int dz = sub % 12;
        return new int[]{tg, dz};
    }

    /**
     * 干支纪月
     *
     * @param yZ 月支
     * @param nG 年干
     */
    public static int[] getGZMonth(int nG, int yZ) {
        //月干
        int i1 = ((nG + 1) * 2 + yZ) % 10 - 1;
        if (i1 == -1) i1 = 9;
        return new int[]{i1, (yZ + 1) % 12};
    }

    /**
     * 干支纪日
     */
    public static int[] getGZDay(LocalDate localDate) {
        long sub = ChronoUnit.DAYS.between(CCConstants.JZ_DAY, localDate);
        if (sub > 0) {
            sub = sub % 60;
        } else {
            sub = sub % 60 + 60;
        }
        int tg = (int) (sub % 10);
        int dz = (int) (sub % 12);
        return new int[]{tg, dz};
    }

    /**
     * 干支计时
     *
     * @param rG  日干
     * @param now LocalDateTime
     */
    public static int[] getGZHour(LocalDateTime now, int rG) {
        int hour = now.getHour();
        int start = switch (rG) {
            //乙/庚 丙
            case 1, 6 -> 2;
            //丙/辛 戊
            case 2, 7 -> 4;
            //丁/壬 庚
            case 3, 8 -> 6;
            //戊/癸 壬
            case 4, 9 -> 8;
            //甲/已 甲
            default -> 0;
        };
        int index = (int) Math.floor((hour + 1) / 2D);
        start += index;
        while (start >= 10) {
            start -= 10;
        }
        index %= 12;
        return new int[]{start, index};
    }

    public static String getGzStr(int[] gz) {
        return CCConstants.TG[gz[0]] + CCConstants.DZ[gz[1]];
    }

    /**
     * @param mZ  月支
     * @param rGZ 日天干
     * @return 胎神
     */
    public static String TaiShen(String mZ, String rGZ) {
        String TaiShen = CCConstants.TAI_SHEN.getString(mZ + rGZ);
        return StringUtil.isEmpty(TaiShen) ? "暂无" : TaiShen;
    }

    /**
     * @param rGZ 日干支
     * @return 彭祖百忌
     */
    public static String PZBJ(int[] rGZ) {
        return CCConstants.TG_PZBJ[rGZ[0]] + "  " + CCConstants.DZ_PZBJ[rGZ[1]];
    }

    /**
     * 日 宜忌
     */
    public static String[] YiJi(LocalDate date, int[] rGZ) {
        int jz = CCConstants.JIA_ZI.indexOf(getGzStr(rGZ));
        int year = date.getYear();
        int yearOffset = year - 1900;
        int offset = date.getDayOfYear();
        int i = 0;
        for (; i < 24; i++) {
            int num = CCConstants.TermTable[yearOffset * 24 + i];
            if (num > offset) {
                break;
            }
        }
        JSONObject yjData = CCConstants.YJ_DATA.getJSONObject(getYJSqlFields(date) + "-" + jz);
        if (yjData == null) return new String[]{"-", "-"};
        String y = yjData.getString("y");
        String j = yjData.getString("j");

        y = StringUtil.isEmpty(y) ? "-" : y;
        j = StringUtil.isEmpty(j) ? "-" : j;

        return new String[]{y, j};
    }

    /**
     * 五行
     */
    public static String WuXing(int[] rGZ) {
        return CCConstants.WuXing.get(getGzStr(rGZ));
    }

    /**
     * 28星宿
     */
    public static String STAR_28(LocalDate date) {
        int B = (date.getYear() - 1) * 365;
        B += date.getDayOfYear();
        //常值为0，，切在3月1日以后(31+29+1)，则为1，其他仍然为0
        int fixValue1 = 0;
        //1900-1999年修正值为13，2000-2099的修正值也为13
        int fixValue2 = 13;
        int year = date.getYear();
        if (isLeapYear(year)) {
            if (date.getMonth().getValue() + 1 > 3 || date.getMonth().getValue() + 1 == 3) {
                fixValue1 = 1;
            }
        }
        var C = Math.floor((double) (date.getYear() - 1) / 4 - fixValue2 + fixValue1);
        var A = B + C;
        int index = (int) ((A + 23) % 28);
        return CCConstants.STAR_28[index];
    }

    /**
     * @param day 农历日
     * @return 月相
     */
    public static String YUE_XIANG(int day) {
        return CCConstants.YUE_XIANG[day];
    }

    /**
     * @param mZ  月支
     * @param rGZ 日天干
     * @return 胎神
     */
    public static String[] JiShenXiongShen(String mZ, String rGZ) {
        JSONObject jsxs = CCConstants.JS_XS_DATA.getJSONObject(mZ + rGZ);
        if (jsxs == null) return new String[]{"暂无", "暂无"};
        String jsyq = jsxs.getString("JSYQ");
        String xsjy = jsxs.getString("XSYJ");
        jsyq = StringUtil.isEmpty(jsyq) ? "暂无" : jsyq;
        xsjy = StringUtil.isEmpty(xsjy) ? "暂无" : xsjy;
        return new String[]{jsyq, xsjy};
    }

    /**
     * 值神
     *
     * @param dayDz   日地支
     * @param monthDz 月地支
     */
    public static String ZhiShen(int monthDz, int dayDz) {
        var qinglongBeginIndex = 0;
        if (monthDz == 0 || monthDz == 6) {
            qinglongBeginIndex = 8;
        } else if (monthDz == 1 || monthDz == 7) {
            qinglongBeginIndex = 10;
        } else if (monthDz == 3 || monthDz == 9) {
            qinglongBeginIndex = 2;
        } else if (monthDz == 4 || monthDz == 10) {
            qinglongBeginIndex = 4;
        } else if (monthDz == 5 || monthDz == 11) {
            qinglongBeginIndex = 6;
        }

        var ishen_12 = (dayDz - qinglongBeginIndex);
        if (ishen_12 < 0) {
            ishen_12 += 12;
        }

        return ZHI_SHEN[ishen_12];
    }

    /**
     * 建除十二神
     *
     * @param thisDate 今日
     * @return 建除十二神
     */
    public static String JianChu12Shen(LocalDate thisDate) {
        var baseDate = LocalDate.of(1901, 1, 1);
        int[] arr = twentyFourTermDaysOf(thisDate);
        int jx = -1;
        if (arr.length == 2) {
            var a = arr[0];
            var b = arr[arr.length - 1];
            var offsetDayCount = a % 2 == 0 ? a / 2 : a / 2 + 1;
            if (b != 0 && a % 2 == 0) {
                offsetDayCount += 1;
            }
            var interval = Math.abs(ChronoUnit.DAYS.between(thisDate, baseDate) * 24 * 60 * 60);
            var day = interval / (24 * 60 * 60);
            jx = (int) Math.ceil((5 + day - offsetDayCount) % 12);
        }
        var jianchuIndex = 0;
        if (jx >= 2) {
            jianchuIndex = jx - 2;
        } else {
            jianchuIndex = jx + 10;
        }


        return JIAN_CHU[jianchuIndex];
    }

    /**
     * 传入支，可得 日冲煞/时冲煞
     */
    public static String ChongSha(int zhi) {
        int cindex = chongIndexOfDateTime(zhi);
        int sindex = shaDirectionOfDateTime(zhi);
        try {
            return "冲" + SHENGXIAO[cindex] + "煞" + COMPASS[sindex];
        } catch (Exception e) {
            return "";
        }
    }

    public static String[] JieQi(LocalDate date) {
        int year = date.getYear();
        int yearOffset = year - 1900;
        yearOffset *= 24;
        int offset = date.getDayOfYear() -1;
        int i = 0;
        boolean JieQi = false;
        for (; i < 24; i++) {
            int num = CCConstants.TermTable[yearOffset + i];
            if (i == 0 && num > offset) {
                break;
            } else if (num > offset) {
                break;
            } else if (num == offset) {
                JieQi = true;
                break;
            }
        }
        String[] sss = new String[5];
        sss[0] = JieQi ? JIE_QI[i] : "";
        //当前差
        int i2 = i - 1;
        i2 = i2 <0 ? 24 + i2 :i2;
        sss[1] = JIE_QI[i2];
        int wholeYear = isLeapYear(year) ? 366 : 365;
        int i1 = offset - TermTable[yearOffset + i - 1];
        if (i1 < 0)
            i1 += wholeYear;
        sss[2] = i1 + 1 + "";
        if (!"".equals(sss[0])) {
            //下一差
            sss[3] = JIE_QI[(Math.abs(i + 1)) % 24];
            int sub = TermTable[yearOffset + i + 1] - offset;
            if (sub < 0) {
                sss[4] = sub + wholeYear + "";
            } else
                sss[4] = sub + "";
        } else if (i != 24) {
            //下一差
            sss[3] = JIE_QI[Math.abs(i % 24)];
            sss[4] = CCConstants.TermTable[yearOffset + i] - offset + "";
        } else {
            int temp = CCConstants.TermTable[yearOffset + i] + wholeYear;
            //下一差
            sss[3] = JIE_QI[Math.abs(i % 24)];
            sss[4] = temp - offset + "";
        }

        return sss;
    }

    /**
     * 逢巳日、酉日、丑日必是“煞东”；亥日、卯日、未日必“煞西”；申日、子日、辰日必“煞南”；寅日、Branch、戌日必“煞北”
     */
    private static int shaDirectionOfDateTime(int branchIndex) {
        return switch (branchIndex) {
            //子辰申
            case 0, 4, 8 -> 4;
            //丑巳酉
            case 1, 5, 9 -> 2;
            //寅午戌
            case 2, 6, 10 -> 0;
            //卯未亥
            case 3, 7, 11 -> 6;
            default -> -1;
        };
    }

    private static boolean isLeapYear(int year) {
        return ((year % 4) == 0) && ((year % 100) != 0) || ((year % 400) == 0);
    }

    /**
     * 子午相冲，丑未相冲，寅申相冲，辰戌相冲，巳亥相冲，卯酉相冲
     */
    private static int chongIndexOfDateTime(int branchIndex) {
        return switch (branchIndex) {
            case 0 -> 6;
            case 1 -> 7;
            case 2 -> 8;
            case 3 -> 9;
            case 4 -> 10;
            case 5 -> 11;
            case 6 -> 0;
            case 7 -> 1;
            case 8 -> 2;
            case 9 -> 3;
            case 10 -> 4;
            case 11 -> 5;
            default -> -1;
        };
    }

    private static int getYJSqlFields(LocalDate date) {
        int[] arr = twentyFourTermDaysOf(date);
        if (arr.length == 2) {
            int a = arr[0];
            int b = arr[1];
            int offsetDayCount = (int) Math.floor(a % 2 == 0 ? a / 2 : a / 2 + 1);
            if (b > 0 && a % 2 == 0) {
                offsetDayCount += 1;
            }
            // 计算当前日期距离1901-1-1多少天
            LocalDate baseDate = LocalDate.of(1901, 1, 1);
            long day = ChronoUnit.DAYS.between(baseDate, date);
            return (int) (Math.floor(Math.abs((5 + day - offsetDayCount) % 12)));
        }
        return -1;
    }

    /**
     * 二十四节气过了哪些
     */
    private static int[] twentyFourTermDaysOf(LocalDate date) {
        try {
            int year = date.getYear();
            int yearOffset = year - 1900;
            int TermTableOffSet = yearOffset * 24;
            int offset = date.getDayOfYear();
            int index = 0;
            int st = 0;// 改日是否为24节气
            for (int i = 0; i < 24; i++) {
                var num = CCConstants.TermTable[TermTableOffSet + i];
                if (num > offset) {
                    index = i;
                    break;
                } else if (num == offset) {
                    index = i;
                    st = 1;
                    break;
                }
            }
            int a = index + yearOffset * 24 - 24;// 莫日之前的节气数目
            int b = st;
            return new int[]{a, b};
        } catch (Exception e) {
            return null;
        }
    }

    public static LunarData calcLunar(LocalDate localDate) {
        int offset = (int) ChronoUnit.DAYS.between(LocalDate.of(1900, 1, 31), localDate);
        LunarData lunarData = new LunarData();
        int i, leap, temp = 0;
        for (i = 1900; i <= 2100 && offset > 0; i++) {
            temp = lYearDays(i);
            offset -= temp;
        }

        if (offset < 0) {
            offset += temp;
            i--;
        }

        int year = i;
        boolean isLeap = false;

        leap = leapMonth(i); //闰哪个月

        for (i = 1; i < 13 && offset > 0; i++) {
            //闰月
            if (leap > 0 && i == (leap + 1) && !isLeap) {
                --i;
                isLeap = true;
                temp = leapDays(year);
            } else {
                temp = monthDays(year, i);
            }
            //解除闰月
            if (isLeap && i == (leap + 1)) {
                isLeap = false;
            }
            offset -= temp;
        }

        if (offset == 0 && leap > 0 && i == leap + 1) if (isLeap) {
            isLeap = false;
        } else {
            isLeap = true;
            --i;
        }

        if (offset < 0) {
            offset += temp;
            --i;
        }

        lunarData.year = year;
        lunarData.month = i;
        lunarData.leap = leap;
        lunarData.day = offset + 1;
        lunarData.isLeap = isLeap;
        return lunarData;
    }

    /**
     * 全年有多少天
     */
    private static int lYearDays(int y) {
        int i;
        //348 是 12个月 农历 全按小月计算也就是29天
        int sum = 348;
        Integer integer = CCConstants.LUNAR_INFO[y - 1900];
        for (i = 0x8000; i > 0x8; i >>= 1) sum += (integer & i) != 0 ? 1 : 0;
        return sum + leapDays(y);
    }

    /**
     * 返回闰月天数
     */
    private static int leapDays(int y) {
        if (leapMonth(y) == 0) return 0;
        return (CCConstants.LUNAR_INFO[y - 1900] & 0x10000) != 0 ? 30 : 29;
    }

    /**
     * 返回润几月 无闰月 返回 0
     */
    private static int leapMonth(int y) {
        return CCConstants.LUNAR_INFO[y - 1900] & 0xf;
    }

    /**
     * 通过位运算，计算出月是否为30天
     */
    private static int monthDays(int y, int m) {
        return (CCConstants.LUNAR_INFO[y - 1900] & (0x10000 >> m)) != 0 ? 30 : 29;
    }
}
