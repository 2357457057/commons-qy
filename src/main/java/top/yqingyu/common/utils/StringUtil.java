package top.yqingyu.common.utils;


import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class StringUtil {

    /**
     * 获取指定索引的指令代码信息
     * @param content : 201223#02#
     * @param i : 1
     * @return : 02
     */
    public static String getSubString(String content,int i){
        content = content.replaceAll("\\*", "#");
        String srtNum ="";
        if (content !=null && !content.equals("") ){
            String phoneNum[] =  content.split("#");
            int index= content.indexOf("#");
            if (index != 0){
                if (phoneNum.length>1 && phoneNum.length >= i){
                    srtNum = phoneNum[i];
                }
            }
        }
        return srtNum;
    }

    /**
     * 获取非空字符串
     * @param obj
     * @return
     */
    public static String getNotNullString(Object obj) {
        if( obj == null || obj.toString().equalsIgnoreCase("null") || obj.toString().length() == 0 )
            return "";
        else
            return obj.toString().trim();
    }

    /**
     * 格式化时间   时分
     * @return
     */
    public static String formatTime(String time, String unit) throws Exception{
        BigInteger divisor1 = new BigInteger("3600000");
        BigInteger divisor2 = new BigInteger("60000");
        BigInteger divisor3 = new BigInteger("3600");
        BigInteger divisor4 = new BigInteger("60");
        BigInteger divideBig = new BigInteger(time);


        if ("毫秒".equals(unit)) {
            BigInteger hourBig = divideBig.divide(divisor1);
            BigInteger minuteBig = (divideBig.subtract(hourBig.multiply(divisor1))).divide(divisor2);
            return hourBig + "小时" + minuteBig +  "分";
        }
        else if ("秒".equals(unit)) {
            BigInteger hourBig = divideBig.divide(divisor3);
            BigInteger minuteBig = (divideBig.subtract(hourBig.multiply(divisor3))).divide(divisor4);
            return hourBig + "小时" + minuteBig +  "分";
        }
        else if ("分钟".equals(unit)) {
            BigInteger hourBig = divideBig.divide(divisor4);
            BigInteger minuteBig = divideBig.subtract(hourBig.multiply(divisor3));
            return hourBig + "小时" + minuteBig +  "分";
        }
        else if ("小时".equals(unit)) {
            return unit + "小时" + "0分";
        }
        else {
            throw new Exception("输入参数有误！");
        }

    }

    /**
     * 格式化两位小数
     * @param dividend 被除数
     * @param divisor 除数
     * @param zeroPoint 是否保留多余0
     * @return
     * @throws Exception
     */
    public static String formatDecimal(String dividend, String divisor,boolean zeroPoint) throws Exception{
        double divideD = Double.parseDouble(dividend);
        double divisorD = Double.parseDouble(divisor);

        String formatStr = "0.00";
        if(divideD == 0){
            return formatStr;
        }

        if (zeroPoint) {
            formatStr = formatDecimal(divideD / divisorD,"0.00");
        }else{
            formatStr = formatDecimal(divideD / divisorD,"0.##");
        }

        return formatStr;
    }

    /**
     * 格式化两位小数
     * @return
     */
    public static String formatDecimal(String dividend, String divisor) throws Exception{
        double divideD = Double.parseDouble(dividend);
        double divisorD = Double.parseDouble(divisor);
        String formatStr = "0.00";

        if (divideD != 0) {
            formatStr = formatDecimal(divideD / divisorD);
        }

        return formatStr;
    }

    /**
     * 格式化两位小数
     * @param str
     * @return
     */
    public static String formatDecimal(Double str,String format){
        if(str.equals("")){
            return "0.00";
        }
        DecimalFormat df=(DecimalFormat)NumberFormat.getInstance();
        df.applyPattern(format);
        return df.format(str);
    }

    /**
     * 格式化两位小数
     * @param str
     * @return
     */
    public static String formatDecimal(Double str){
        if(str.equals("")){
            return "0.00";
        }
        DecimalFormat df=(DecimalFormat)NumberFormat.getInstance();
        df.applyPattern("0.00");
        return df.format(str);
    }



    /**
     * 去除数组中重复的数据
     * @param str
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String[] removeDuplicate(String[] str)
    {
        List retList = new ArrayList();
        for (int i = 0; i < str.length; i++) {
            retList.add(str[i]);
        }
        HashSet h = new HashSet(retList);
        retList.clear();
        retList.addAll(h);

        int size = retList.size();
        String[] retStr = new String[size];
        for (int i = 0; i < size; i++) {
            retStr[i] = retList.get(i).toString();
        }
        return retStr;
    }

    /**
     * 随机获取一定范围的整数
     * @param min 随机最小值
     * @param max 随机最大值
     * @return int
     */
    public static int getRandInteger(int min,int max){
        int number;
        number = (int)(Math.random() * (max) + min);
        return number;
    }

    /**
     * 判断字符串是否为空，为空str赋默认值defaultValue
     * @param str
     * @param defaultValue
     * @return String
     */
    public static String nvl(String str, String defaultValue) {
        if (str==null){
            str = defaultValue;
        }
        return str;
    }


    public static Object timenvl(Object str, int len) throws Exception {
        if (str==null){
            str = "";
            for (int i = 0; i < len; i ++){
                str = "0" + str;
            }
            return TimeUtil.string2Date((String)str);
        }else
            return str;
    }

    /**
     * 全角字符转半角字符
     *
     * @param src
     * @return
     */
    public static String toSemiangle(String src) {
        char[] ca = src.toCharArray();
        for (int idx = 0; idx < ca.length; idx++) {
            if (ca[idx] == 0x3000) {
                ca[idx] = (char) 0x20;
            } else if (ca[idx] > 0xFF00 && ca[idx] < 0xFF5F) {
                ca[idx] = (char) (ca[idx] - 0xFEE0);
            }
        }
        return String.valueOf(ca);
    }

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
