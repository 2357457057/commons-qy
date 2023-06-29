package top.yqingyu.common.qydata;

import com.alibaba.fastjson2.JSON;
import top.yqingyu.common.utils.ClazzUtil;

import java.io.Serial;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.qydata.ConcurrentDataMap
 * @description
 * @createTime 2022年09月17日 10:59:00
 */
public class ConcurrentDataMap<K, V> extends ConcurrentReferenceHashMap<K, V> implements ConcurrentMap<K, V>, Serializable, Cloneable {
    @Serial
    private static final long serialVersionUID = 5720465655020589716L;

    public ConcurrentDataMap() {
    }

    public ConcurrentDataMap(int initialCapacity) {
        super(initialCapacity);
    }

    public ConcurrentDataMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ConcurrentDataMap(int initialCapacity, int concurrencyLevel) {
        super(initialCapacity, concurrencyLevel);
    }

    public ConcurrentDataMap(int initialCapacity, ReferenceType referenceType) {
        super(initialCapacity, referenceType);
    }

    public ConcurrentDataMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        super(initialCapacity, loadFactor, concurrencyLevel);
    }

    public ConcurrentDataMap(int initialCapacity, float loadFactor, int concurrencyLevel, ReferenceType referenceType) {
        super(initialCapacity, loadFactor, concurrencyLevel, referenceType);
    }

    /**
     * @param name 时间单位参数
     * @return 返回通用时间单位
     * @author YYJ
     * @version 1.0.0
     * @description 获取数据中的时间单位, 不区分大小写
     */
    public TimeUnit getTimeUnit(K name) {
        String value = this.getString(name, "");
        if ("NANOSECONDS".equals(value.toUpperCase()))
            return TimeUnit.NANOSECONDS;

        if ("MICROSECONDS".equals(value.toUpperCase()))
            return TimeUnit.MICROSECONDS;

        if ("MILLISECONDS".equals(value.toUpperCase()))
            return TimeUnit.MILLISECONDS;

        if ("SECONDS".equals(value.toUpperCase()))
            return TimeUnit.SECONDS;

        if ("MINUTES".equals(value.toUpperCase()))
            return TimeUnit.MINUTES;

        if ("HOURS".equals(value.toUpperCase()))
            return TimeUnit.HOURS;

        if ("DAYS".equals(value.toUpperCase()))
            return TimeUnit.DAYS;

        return null;
    }

    /**
     * @param name         时间单位参数
     * @param defaultValue 默认值
     * @return 返回通用时间单位
     * @author YYJ
     * @version 1.0.0
     * @description 获取数据中的时间单位, 不区分大小写
     */
    public TimeUnit getTimeUnit(K name, TimeUnit defaultValue) {
        TimeUnit timeUnit = getTimeUnit(name);
        return timeUnit == null ? defaultValue : timeUnit;

    }


    public String getString(K key) {
        V v = this.get(key);
        if (v == null) return null;
        if (ClazzUtil.canValueof(v.getClass())) {
            return String.valueOf(v);
        } else {
            return null;
        }
    }

    public String getString(K key, String defaultValue) {
        String value = this.getString(key);
        return value == null ? defaultValue : value;
    }

    public Integer getInteger(K key) {
        String v = this.getString(key);
        if (v == null) return null;
        return Integer.valueOf(v);
    }

    public Integer getInteger(K key, Integer defaultValue) {
        Integer value = this.getInteger(key);
        return value == null ? defaultValue : value;
    }

    public Long getLong(K key) {
        String v = this.getString(key);
        if (v == null) return null;
        return Long.valueOf(v);
    }

    public Long getLong(K key, Long defaultValue) {
        Long value = this.getLong(key);
        return value == null ? defaultValue : value;
    }

    @SuppressWarnings("unchecked")
    public ConcurrentDataMap<K, V> getConcurrentDataMap(K k) {
        V v = this.get(k);
        if (v instanceof ConcurrentDataMap<?, ?> map)
            return (ConcurrentDataMap<K, V>) map;
        else if (v instanceof Map<?, ?> map) {
            ConcurrentDataMap<K, V> dataMap = new ConcurrentDataMap<>();
            this.replace(k, (V) map);
            return dataMap;
        } else return null;
    }

    @SuppressWarnings("unchecked")
    public void putAll2(Map<?, ?> m) {
        for (Map.Entry<?, ?> e : m.entrySet())
            put((K) e.getKey(), (V) e.getValue());
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
    public Charset getCharSet(K name) {
        String charset = this.getString(name,"");
        charset = charset.toUpperCase();
        try {
            return switch (charset) {
                case "UTF-8" -> StandardCharsets.UTF_8;
                case "GBK" -> Charset.forName("GBK");
                case "ISO-8859-1" -> StandardCharsets.ISO_8859_1;
                case "US-ASCII" -> StandardCharsets.US_ASCII;
                case "UTF-16" -> StandardCharsets.UTF_16;
                case "UTF-16LE" -> StandardCharsets.UTF_16LE;
                case "UTF-16BE" -> StandardCharsets.UTF_16BE;
                case "GB2312" -> Charset.forName("GB2312");
                case "BIG5" -> Charset.forName("Big5");
                case "BIG5-HKSCS" -> Charset.forName("Big5-HKSCS");
                case "CESU-8" -> Charset.forName("CESU-8");
                case "EUC-JP" -> Charset.forName("EUC-JP");
                case "EUC-KR" -> Charset.forName("EUC-KR");
                case "GB18030" -> Charset.forName("GB18030");
                case "IBM-THAI" -> Charset.forName("IBM-Thai");
                case "IBM00858" -> Charset.forName("IBM00858");
                case "IBM01140" -> Charset.forName("IBM01140");
                case "IBM01141" -> Charset.forName("IBM01141");
                case "IBM01142" -> Charset.forName("IBM01142");
                case "IBM01143" -> Charset.forName("IBM01143");
                case "IBM01144" -> Charset.forName("IBM01144");
                case "IBM01145" -> Charset.forName("IBM01145");
                case "IBM01146" -> Charset.forName("IBM01146");
                case "IBM01147" -> Charset.forName("IBM01147");
                case "IBM01148" -> Charset.forName("IBM01148");
                case "IBM01149" -> Charset.forName("IBM01149");
                case "IBM037" -> Charset.forName("IBM037");
                case "IBM1026" -> Charset.forName("IBM1026");
                case "IBM1047" -> Charset.forName("IBM1047");
                case "IBM273" -> Charset.forName("IBM273");
                case "IBM277" -> Charset.forName("IBM277");
                case "IBM278" -> Charset.forName("IBM278");
                case "IBM280" -> Charset.forName("IBM280");
                case "IBM284" -> Charset.forName("IBM284");
                case "IBM285" -> Charset.forName("IBM285");
                case "IBM290" -> Charset.forName("IBM290");
                case "IBM297" -> Charset.forName("IBM297");
                case "IBM420" -> Charset.forName("IBM420");
                case "IBM424" -> Charset.forName("IBM424");
                case "IBM437" -> Charset.forName("IBM437");
                case "IBM500" -> Charset.forName("IBM500");
                case "IBM775" -> Charset.forName("IBM775");
                case "IBM850" -> Charset.forName("IBM850");
                case "IBM852" -> Charset.forName("IBM852");
                case "IBM855" -> Charset.forName("IBM855");
                case "IBM857" -> Charset.forName("IBM857");
                case "IBM860" -> Charset.forName("IBM860");
                case "IBM861" -> Charset.forName("IBM861");
                case "IBM862" -> Charset.forName("IBM862");
                case "IBM863" -> Charset.forName("IBM863");
                case "IBM864" -> Charset.forName("IBM864");
                case "IBM865" -> Charset.forName("IBM865");
                case "IBM866" -> Charset.forName("IBM866");
                case "IBM868" -> Charset.forName("IBM868");
                case "IBM869" -> Charset.forName("IBM869");
                case "IBM870" -> Charset.forName("IBM870");
                case "IBM871" -> Charset.forName("IBM871");
                case "IBM918" -> Charset.forName("IBM918");
                case "ISO-2022-CN" -> Charset.forName("ISO-2022-CN");
                case "ISO-2022-JP" -> Charset.forName("ISO-2022-JP");
                case "ISO-2022-JP-2" -> Charset.forName("ISO-2022-JP-2");
                case "ISO-2022-KR" -> Charset.forName("ISO-2022-KR");
                case "ISO-8859-13" -> Charset.forName("ISO-8859-13");
                case "ISO-8859-15" -> Charset.forName("ISO-8859-15");
                case "ISO-8859-16" -> Charset.forName("ISO-8859-16");
                case "ISO-8859-2" -> Charset.forName("ISO-8859-2");
                case "ISO-8859-3" -> Charset.forName("ISO-8859-3");
                case "ISO-8859-4" -> Charset.forName("ISO-8859-4");
                case "ISO-8859-5" -> Charset.forName("ISO-8859-5");
                case "ISO-8859-6" -> Charset.forName("ISO-8859-6");
                case "ISO-8859-7" -> Charset.forName("ISO-8859-7");
                case "ISO-8859-8" -> Charset.forName("ISO-8859-8");
                case "ISO-8859-9" -> Charset.forName("ISO-8859-9");
                case "JIS_X0201" -> Charset.forName("JIS_X0201");
                case "JIS_X0212-1990" -> Charset.forName("JIS_X0212-1990");
                case "KOI8-R" -> Charset.forName("KOI8-R");
                case "KOI8-U" -> Charset.forName("KOI8-U");
                case "SHIFT_JIS" -> Charset.forName("Shift_JIS");
                case "TIS-620" -> Charset.forName("TIS-620");
                case "UTF-32" -> Charset.forName("UTF-32");
                case "UTF-32BE" -> Charset.forName("UTF-32BE");
                case "UTF-32LE" -> Charset.forName("UTF-32LE");
                case "WINDOWS-1250" -> Charset.forName("windows-1250");
                case "WINDOWS-1251" -> Charset.forName("windows-1251");
                case "WINDOWS-1252" -> Charset.forName("windows-1252");
                case "WINDOWS-1253" -> Charset.forName("windows-1253");
                case "WINDOWS-1254" -> Charset.forName("windows-1254");
                case "WINDOWS-1255" -> Charset.forName("windows-1255");
                case "WINDOWS-1256" -> Charset.forName("windows-1256");
                case "WINDOWS-1257" -> Charset.forName("windows-1257");
                case "WINDOWS-1258" -> Charset.forName("windows-1258");
                case "WINDOWS-31J" -> Charset.forName("windows-31j");
                case "X-BIG5-HKSCS-2001" -> Charset.forName("x-Big5-HKSCS-2001");
                case "X-BIG5-SOLARIS" -> Charset.forName("x-Big5-Solaris");
                case "X-EUC-JP-LINUX" -> Charset.forName("x-euc-jp-linux");
                case "X-EUC-TW" -> Charset.forName("x-EUC-TW");
                case "X-EUCJP-OPEN" -> Charset.forName("x-eucJP-Open");
                case "X-IBM1006" -> Charset.forName("x-IBM1006");
                case "X-IBM1025" -> Charset.forName("x-IBM1025");
                case "X-IBM1046" -> Charset.forName("x-IBM1046");
                case "X-IBM1097" -> Charset.forName("x-IBM1097");
                case "X-IBM1098" -> Charset.forName("x-IBM1098");
                case "X-IBM1112" -> Charset.forName("x-IBM1112");
                case "X-IBM1122" -> Charset.forName("x-IBM1122");
                case "X-IBM1123" -> Charset.forName("x-IBM1123");
                case "X-IBM1124" -> Charset.forName("x-IBM1124");
                case "X-IBM1129" -> Charset.forName("x-IBM1129");
                case "X-IBM1166" -> Charset.forName("x-IBM1166");
                case "X-IBM1364" -> Charset.forName("x-IBM1364");
                case "X-IBM1381" -> Charset.forName("x-IBM1381");
                case "X-IBM1383" -> Charset.forName("x-IBM1383");
                case "X-IBM29626C" -> Charset.forName("x-IBM29626C");
                case "X-IBM300" -> Charset.forName("x-IBM300");
                case "X-IBM33722" -> Charset.forName("x-IBM33722");
                case "X-IBM737" -> Charset.forName("x-IBM737");
                case "X-IBM833" -> Charset.forName("x-IBM833");
                case "X-IBM834" -> Charset.forName("x-IBM834");
                case "X-IBM856" -> Charset.forName("x-IBM856");
                case "X-IBM874" -> Charset.forName("x-IBM874");
                case "X-IBM875" -> Charset.forName("x-IBM875");
                case "X-IBM921" -> Charset.forName("x-IBM921");
                case "X-IBM922" -> Charset.forName("x-IBM922");
                case "X-IBM930" -> Charset.forName("x-IBM930");
                case "X-IBM933" -> Charset.forName("x-IBM933");
                case "X-IBM935" -> Charset.forName("x-IBM935");
                case "X-IBM937" -> Charset.forName("x-IBM937");
                case "X-IBM939" -> Charset.forName("x-IBM939");
                case "X-IBM942" -> Charset.forName("x-IBM942");
                case "X-IBM942C" -> Charset.forName("x-IBM942C");
                case "X-IBM943" -> Charset.forName("x-IBM943");
                case "X-IBM943C" -> Charset.forName("x-IBM943C");
                case "X-IBM948" -> Charset.forName("x-IBM948");
                case "X-IBM949" -> Charset.forName("x-IBM949");
                case "X-IBM949C" -> Charset.forName("x-IBM949C");
                case "X-IBM950" -> Charset.forName("x-IBM950");
                case "X-IBM964" -> Charset.forName("x-IBM964");
                case "X-IBM970" -> Charset.forName("x-IBM970");
                case "X-ISCII91" -> Charset.forName("x-ISCII91");
                case "X-ISO-2022-CN-CNS" -> Charset.forName("x-ISO-2022-CN-CNS");
                case "X-ISO-2022-CN-GB" -> Charset.forName("x-ISO-2022-CN-GB");
                case "X-ISO-8859-11" -> Charset.forName("x-iso-8859-11");
                case "X-JIS0208" -> Charset.forName("x-JIS0208");
                case "X-JISAUTODETECT" -> Charset.forName("x-JISAutoDetect");
                case "X-JOHAB" -> Charset.forName("x-Johab");
                case "X-MACARABIC" -> Charset.forName("x-MacArabic");
                case "X-MACCENTRALEUROPE" -> Charset.forName("x-MacCentralEurope");
                case "X-MACCROATIAN" -> Charset.forName("x-MacCroatian");
                case "X-MACCYRILLIC" -> Charset.forName("x-MacCyrillic");
                case "X-MACDINGBAT" -> Charset.forName("x-MacDingbat");
                case "X-MACGREEK" -> Charset.forName("x-MacGreek");
                case "X-MACHEBREW" -> Charset.forName("x-MacHebrew");
                case "X-MACICELAND" -> Charset.forName("x-MacIceland");
                case "X-MACROMAN" -> Charset.forName("x-MacRoman");
                case "X-MACROMANIA" -> Charset.forName("x-MacRomania");
                case "X-MACSYMBOL" -> Charset.forName("x-MacSymbol");
                case "X-MACTHAI" -> Charset.forName("x-MacThai");
                case "X-MACTURKISH" -> Charset.forName("x-MacTurkish");
                case "X-MACUKRAINE" -> Charset.forName("x-MacUkraine");
                case "X-MS932_0213" -> Charset.forName("x-MS932_0213");
                case "X-MS950-HKSCS" -> Charset.forName("x-MS950-HKSCS");
                case "X-MS950-HKSCS-XP" -> Charset.forName("x-MS950-HKSCS-XP");
                case "X-MSWIN-936" -> Charset.forName("x-mswin-936");
                case "X-PCK" -> Charset.forName("x-PCK");
                case "X-SJIS_0213" -> Charset.forName("x-SJIS_0213");
                case "X-UTF-16LE-BOM" -> Charset.forName("x-UTF-16LE-BOM");
                case "X-UTF-32BE-BOM" -> Charset.forName("X-UTF-32BE-BOM");
                case "X-UTF-32LE-BOM" -> Charset.forName("X-UTF-32LE-BOM");
                case "X-WINDOWS-50220" -> Charset.forName("x-windows-50220");
                case "X-WINDOWS-50221" -> Charset.forName("x-windows-50221");
                case "X-WINDOWS-874" -> Charset.forName("x-windows-874");
                case "X-WINDOWS-949" -> Charset.forName("x-windows-949");
                case "X-WINDOWS-950" -> Charset.forName("x-windows-950");
                case "X-WINDOWS-ISO2022JP" -> Charset.forName("x-windows-iso2022jp");
                default -> null;
            };
        } catch (UnsupportedCharsetException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Charset getCharSet(K name, Charset defaultValue) {
        Charset charSet = this.getCharSet(name);
        return charSet == null ? defaultValue : charSet;
    }

    public Charset getCharSetDef2$UTF8(K name, K defaultValue) {
        Charset charSet = this.getCharSet(name);
        return charSet != null ? charSet : this.getCharSet(defaultValue, StandardCharsets.UTF_8);
    }
    @Override
    @SuppressWarnings("all")
    public ConcurrentDataMap clone() {
        try {
            return (ConcurrentDataMap) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
