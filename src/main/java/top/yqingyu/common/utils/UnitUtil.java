package top.yqingyu.common.utils;



import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 数据单位转换工具
 * @author YYJ
 * @version 1.0.0
 * @Classvalue top.yqingyu.common.utils.UnitUtil
 * @createTime 2022年09月18日 22:10:00
 */
public class UnitUtil {
    private static final Map<String, Double> TIME_UNIT = new IdentityHashMap<>(10);
    private static final Map<String, Double> BYTE_UNIT = new IdentityHashMap<>(7);
    private static final String UNIT_STR = "[0-9*/. ]*";

    public static final String TB = UNIT_STR + "(TB|T)";
    public static final String GB = UNIT_STR + "(GB|G)";
    public static final String MB = UNIT_STR + "(MB|M)";
    public static final String KB = UNIT_STR + "(KB|K)";
    public static final String B = UNIT_STR + "B";

    public static final String YEAR = UNIT_STR + "YEAR";
    public static final String QUARTER = UNIT_STR + "QUARTER";

    public static final String MONTH = UNIT_STR + "MONTH";
    public static final String WEEK = UNIT_STR + "WEEK";
    public static final String DAY = UNIT_STR + "DAY";
    public static final String H = UNIT_STR + "H";
    public static final String MIN = UNIT_STR + "MIN";
    public static final String S = UNIT_STR + "S";
    public static final String MILLS = UNIT_STR + "MILLS";
    public static final String MICOS = UNIT_STR + "MICOS";
    public static final String NANOS = UNIT_STR + "NANOS";


    static {
        BYTE_UNIT.put(B, 1D);
        BYTE_UNIT.put(KB, BYTE_UNIT.get(B) * 1024);
        BYTE_UNIT.put(MB, BYTE_UNIT.get(KB) * 1024);
        BYTE_UNIT.put(GB, BYTE_UNIT.get(MB) * 1024);
        BYTE_UNIT.put(TB, BYTE_UNIT.get(GB) * 1024);


        TIME_UNIT.put(NANOS, 1D);
        TIME_UNIT.put(MICOS, TIME_UNIT.get(NANOS) * 1000);
        TIME_UNIT.put(MILLS, TIME_UNIT.get(MICOS) * 1000);
        TIME_UNIT.put(S, TIME_UNIT.get(MILLS) * 1000);
        TIME_UNIT.put(MIN, TIME_UNIT.get(S) * 60);
        TIME_UNIT.put(H, TIME_UNIT.get(MIN) * 60);
        TIME_UNIT.put(DAY, TIME_UNIT.get(H) * 24);
        TIME_UNIT.put(WEEK, TIME_UNIT.get(DAY) * 7);
        TIME_UNIT.put(YEAR, TIME_UNIT.get(DAY) * 365);
        TIME_UNIT.put(MONTH, TIME_UNIT.get(YEAR) / 12);
        TIME_UNIT.put(QUARTER, TIME_UNIT.get(YEAR) / 4);
    }


    public static Long $2B(String value) {
        return turnLong2(StringUtil.isBlank(value) ? "" :value, BYTE_UNIT.get(B), BYTE_UNIT);
    }

    public static Long $2KB(String value) {
        return turnLong2(StringUtil.isBlank(value) ? "" :value, BYTE_UNIT.get(KB), BYTE_UNIT);
    }

    public static Long $2MB(String value) {
        return turnLong2(StringUtil.isBlank(value) ? "" :value, BYTE_UNIT.get(MB), BYTE_UNIT);
    }

    public static Long $2GB(String value) {
        return turnLong2(StringUtil.isBlank(value) ? "" :value, BYTE_UNIT.get(GB), BYTE_UNIT);
    }

    public static Long $2TB(String value) {
        return turnLong2(StringUtil.isBlank(value) ? "" :value, BYTE_UNIT.get(TB), BYTE_UNIT);
    }

    public static Long $2YEAR(String value) {
        return turnLong2(StringUtil.isBlank(value) ? "" :value, TIME_UNIT.get(YEAR), TIME_UNIT);
    }

    public static Long $2QUARTER(String value) {
        return turnLong2(StringUtil.isBlank(value) ? "" :value, TIME_UNIT.get(QUARTER), TIME_UNIT);
    }

    public static Long $2MONTH(String value) {
        return turnLong2(StringUtil.isBlank(value) ? "" :value, TIME_UNIT.get(MONTH), TIME_UNIT);
    }

    public static Long $2WEEK(String value) {
        return turnLong2(StringUtil.isBlank(value) ? "" :value, TIME_UNIT.get(WEEK), TIME_UNIT);
    }

    public static Long $2DAY(String value) {
        return turnLong2(StringUtil.isBlank(value) ? "" :value, TIME_UNIT.get(DAY), TIME_UNIT);
    }

    public static Long $2H(String value) {
        return turnLong2(StringUtil.isBlank(value) ? "" :value, TIME_UNIT.get(H), TIME_UNIT);
    }

    public static Long $2MIN(String value) {
        return turnLong2(StringUtil.isBlank(value) ? "" :value, TIME_UNIT.get(MIN), TIME_UNIT);
    }

    public static Long $2S(String value) {
        return turnLong2(StringUtil.isBlank(value) ? "" :value, TIME_UNIT.get(S), TIME_UNIT);
    }

    public static Long $2MILLS(String value) {
        return turnLong2(StringUtil.isBlank(value) ? "" :value, TIME_UNIT.get(MILLS), TIME_UNIT);
    }

    public static Long $2MICOS(String value) {
        return turnLong2(StringUtil.isBlank(value) ? "" :value, TIME_UNIT.get(MICOS), TIME_UNIT);
    }

    public static Long $2NANOS(String value) {
        return turnLong2(StringUtil.isBlank(value) ? "" :value, TIME_UNIT.get(NANOS), TIME_UNIT);
    }


    /**
     * 带单位值转目标单位 支持乘除法小数
     *
     * @param value 乘法单位表达式
     * @param targetUnit 目标单位
     * @return 目标单位数值
     */
    public static Long turnLong2(String value, Double targetUnit, Map<String, Double> UNIT_MAP) {
        AtomicReference<Long> $return = new AtomicReference<>();
        UNIT_MAP.forEach((k, v) -> {
            if (value.matches(k)) {
                AtomicReference<Double> noUnitValue = new AtomicReference<>(1D);
                //去掉正则中数字部
                String unitStr = k.replace(UNIT_STR, "");
                //去掉值中单位部
                String $value = value.replaceAll(unitStr, "");
                //值的乘除法
                Arrays.stream($value.split("[*]")).forEach((s1 -> {
                    String[] split = s1.split("[/]");
                    if (split.length > 1) { //当有除法运算符
                        AtomicInteger i = new AtomicInteger();
                        Arrays.stream(split).forEach((s2 -> {
                            if (i.getAndIncrement() == 0) {//首位应先做乘法
                                if (!"".equals(s2))
                                    noUnitValue.updateAndGet(c -> c * Double.parseDouble(s2));
                            } else if (!"".equals(s2))
                                noUnitValue.updateAndGet(c -> c / Double.parseDouble(s2));
                        }));
                    } else if (!"".equals(s1))
                        noUnitValue.updateAndGet(c -> c * Double.parseDouble(s1));
                }));
                //值*单位
                $return.set((long) (v * noUnitValue.get() / targetUnit));
            }
        });
        return $return.get();
    }

    /**
     * 带单位值转目标单位 只有乘法
     *
     * @param value 乘法单位表达式
     * @param targetUnit 目标单位
     * @return 目标单位数值2
     */
    public static Long turnLong(String value, Double targetUnit, Map<String, Double> UNIT_MAP) {
        AtomicReference<Long> $return = new AtomicReference<>();
        UNIT_MAP.forEach((k, v) -> {
            if (value.matches(k)) {
                AtomicReference<Double> d = new AtomicReference<>(1D);
                //去掉正则中数字部
                String unitStr = k.replace(UNIT_STR, "");
                //去掉值中单位部
                String $value = value.replaceAll(unitStr, "");
                //值乘法
                Arrays.stream($value.split("[*]"))
                        .forEach((s1 -> {
                            if (!"".equals(s1))
                                d.updateAndGet(c -> c * Double.parseDouble(s1));
                        }));
                //值*单位
                $return.set((long) (v * d.get() / targetUnit));
            }
        });
        return $return.get();
    }

}
