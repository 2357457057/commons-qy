package top.yqingyu.common.utils;

import java.util.ArrayList;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.utils.ArrayUtil
 * @Description 数组相关操作
 * @createTime 2022年09月03日 08:12:00
 */
public class ArrayUtil {

    /**
     * 切割数组
     *
     * @param bytes      需要被分割的数组
     * @param LENGTH_MAX 每个数组的最大长度
     * @return 被分割bytes的list
     */

    public static ArrayList<byte[]> checkArrayLength(byte[] bytes, int LENGTH_MAX) {
        ArrayList<byte[]> list = new ArrayList<>();
        double l = bytes.length;
        double copyTimes = Math.ceil(l / LENGTH_MAX);
        int srcPos = 0;
        if (l <= LENGTH_MAX)
            list.add(bytes);
        else {
            for (int i = 0; i < copyTimes; i++) {
                byte[] current;
                if (i == copyTimes - 1)
                    //最后一个数组长度初始化
                    current = new byte[(int) (l - srcPos)];
                else
                    current = new byte[LENGTH_MAX];
                System.arraycopy(bytes, srcPos, current, 0, current.length);
                srcPos += LENGTH_MAX;
                list.add(current);
            }
        }
        return list;
    }


    public static ArrayList<short[]> checkArrayLength(short[] bytes, int LENGTH_MAX) {
        ArrayList<short[]> list = new ArrayList<>();
        double l = bytes.length;
        double copyTimes = Math.ceil(l / LENGTH_MAX);
        int srcPos = 0;
        if (l <= LENGTH_MAX)
            list.add(bytes);
        else {
            for (int i = 0; i < copyTimes; i++) {
                short[] current;
                if (i == copyTimes - 1)
                    //最后一个数组长度初始化
                    current = new short[(int) (l - srcPos)];
                else
                    current = new short[LENGTH_MAX];
                System.arraycopy(bytes, srcPos, current, 0, current.length);
                srcPos += LENGTH_MAX;
                list.add(current);
            }
        }
        return list;
    }

    public static ArrayList<int[]> checkArrayLength(int[] bytes, int LENGTH_MAX) {
        ArrayList<int[]> list = new ArrayList<>();
        double l = bytes.length;
        double copyTimes = Math.ceil(l / LENGTH_MAX);
        int srcPos = 0;
        if (l <= LENGTH_MAX)
            list.add(bytes);
        else {
            for (int i = 0; i < copyTimes; i++) {
                int[] current;
                if (i == copyTimes - 1)
                    //最后一个数组长度初始化
                    current = new int[(int) (l - srcPos)];
                else
                    current = new int[LENGTH_MAX];
                System.arraycopy(bytes, srcPos, current, 0, current.length);
                srcPos += LENGTH_MAX;
                list.add(current);
            }
        }
        return list;
    }

    public static ArrayList<long[]> checkArrayLength(long[] bytes, int LENGTH_MAX) {
        ArrayList<long[]> list = new ArrayList<>();
        double l = bytes.length;
        double copyTimes = Math.ceil(l / LENGTH_MAX);
        int srcPos = 0;
        if (l <= LENGTH_MAX)
            list.add(bytes);
        else {
            for (int i = 0; i < copyTimes; i++) {
                long[] current;
                if (i == copyTimes - 1)
                    //最后一个数组长度初始化
                    current = new long[(int) (l - srcPos)];
                else
                    current = new long[LENGTH_MAX];
                System.arraycopy(bytes, srcPos, current, 0, current.length);
                srcPos += LENGTH_MAX;
                list.add(current);
            }
        }
        return list;
    }

    public static ArrayList<float[]> checkArrayLength(float[] bytes, int LENGTH_MAX) {
        ArrayList<float[]> list = new ArrayList<>();
        double l = bytes.length;
        double copyTimes = Math.ceil(l / LENGTH_MAX);
        int srcPos = 0;
        if (l <= LENGTH_MAX)
            list.add(bytes);
        else {
            for (int i = 0; i < copyTimes; i++) {
                float[] current;
                if (i == copyTimes - 1)
                    //最后一个数组长度初始化
                    current = new float[(int) (l - srcPos)];
                else
                    current = new float[LENGTH_MAX];
                System.arraycopy(bytes, srcPos, current, 0, current.length);
                srcPos += LENGTH_MAX;
                list.add(current);
            }
        }
        return list;
    }

    public static ArrayList<double[]> checkArrayLength(double[] bytes, int LENGTH_MAX) {
        ArrayList<double[]> list = new ArrayList<>();
        double l = bytes.length;
        double copyTimes = Math.ceil(l / LENGTH_MAX);
        int srcPos = 0;
        if (l <= LENGTH_MAX)
            list.add(bytes);
        else {
            for (int i = 0; i < copyTimes; i++) {
                double[] current;
                if (i == copyTimes - 1)
                    //最后一个数组长度初始化
                    current = new double[(int) (l - srcPos)];
                else
                    current = new double[LENGTH_MAX];
                System.arraycopy(bytes, srcPos, current, 0, current.length);
                srcPos += LENGTH_MAX;
                list.add(current);
            }
        }
        return list;
    }

    public static ArrayList<char[]> checkArrayLength(char[] bytes, int LENGTH_MAX) {
        ArrayList<char[]> list = new ArrayList<>();
        double l = bytes.length;
        double copyTimes = Math.ceil(l / LENGTH_MAX);
        int srcPos = 0;
        if (l <= LENGTH_MAX)
            list.add(bytes);
        else {
            for (int i = 0; i < copyTimes; i++) {
                char[] current;
                if (i == copyTimes - 1)
                    //最后一个数组长度初始化
                    current = new char[(int) (l - srcPos)];
                else
                    current = new char[LENGTH_MAX];
                System.arraycopy(bytes, srcPos, current, 0, current.length);
                srcPos += LENGTH_MAX;
                list.add(current);
            }
        }
        return list;
    }
}
