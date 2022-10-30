package top.yqingyu.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.utils.ArrayUtil
 * @Description The array-related operations. partly original and partly copied from commons-lang3   ^ u ^
 * @createTime 2022年09月03日 08:12:00
 */
@SuppressWarnings("all")
public class ArrayUtil {

    public static final byte[] RN = "\r\n".getBytes(StandardCharsets.UTF_8);
    public static final byte[] RN_RN = "\r\n\r\n".getBytes(StandardCharsets.UTF_8);
    public static final byte[] SPACE = " ".getBytes(StandardCharsets.UTF_8);
    public static final byte[] COLON_SPACE = ": ".getBytes(StandardCharsets.UTF_8);
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

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

    /**
     * 寻找目标数组在源数组中的索引起始值
     *
     * @param source 源数组
     * @param target 目标数组
     * @return 索引值的集合
     * @author YYJ
     * @description
     */
    public static ArrayList<Integer> indexOfTarget(byte[] source, byte[] target) {
        AtomicInteger enumerator = new AtomicInteger();
        ArrayList<Integer> list = new ArrayList<>();
        int targetLength = target.length;
        for (int i = 0; i < source.length; i++) {
            int tIdx = enumerator.getAndIncrement();
            if (source[i] == target[tIdx] && tIdx + 1 == targetLength) {
                list.add(i - targetLength + 1);
                enumerator.set(0);
            } else if (source[i] != target[tIdx]) {
                i -= tIdx;
                enumerator.set(0);
            }
        }
        return list;
    }

    /**
     * 寻找目标数组的索引起始值
     *
     * @param source 源数组
     * @param target 目标数组
     * @return 索引值的集合
     * @author YYJ
     * @description
     */
    public static int firstIndexOfTarget(byte[] source, byte[] target) {
        AtomicInteger enumerator = new AtomicInteger();
        int targetLength = target.length;
        for (int i = 0; i < source.length; i++) {
            int tIdx = enumerator.getAndIncrement();
            if (source[i] == target[tIdx] && tIdx + 1 == targetLength) {
                return i - targetLength + 1;
            } else if (source[i] != target[tIdx]) {
                enumerator.set(0);
            }
        }
        return -1;
    }


//
//    public static byte[] replace(byte[] source, byte[] oldB, byte[] newB) {
//        ArrayList<Integer> list = indexOfTarget(source, oldB);
//        int size = list.size();
//
//        if (size == 0) {
//            return source;
//        }
//        int newLength = source.length + (newB.length - oldB.length) * size;
//        byte[] newBytes = new byte[newLength];
//        int srcPos = 0;
//        int destPos = 0;
//        for (Integer integer : list) {
//
//            System.arraycopy(source, srcPos, newBytes, destPos, integer + 1);
//            System.arraycopy(newB, 0, newBytes, integer, newB.length);
//            srcPos = integer + oldB.length;
//            destPos = integer + newB.length;
//
//        }
//        return newBytes;
//    }
//
//    public static byte[] replaceAll(byte[] source, byte[] target) {
//
//    }

    /**
     * 依据目标数组拆分源数组
     *
     * @param source 源数组
     * @param target 目标数组
     * @return 拆分合集
     * @author YYJ
     * @description
     */
    public static ArrayList<byte[]> splitByTarget(byte[] source, byte[] target) {
        ArrayList<byte[]> bytes = new ArrayList<>();
        ArrayList<Integer> list = indexOfTarget(source, target);

        int targetLength = target.length;
        int sourceLength = source.length;
        int preIdx = 0;
        int currentIdx;
        int currentLength;
        int indexListLength = list.size();

        for (int i = 0; i < indexListLength; i++) {
            currentIdx = list.get(i);
            //当前索引向前切割
            currentLength = currentIdx - preIdx;
            byte[] buf = new byte[currentLength];
            System.arraycopy(source, preIdx, buf, 0, currentLength);
            bytes.add(buf);
            //最后一个索引向后切割
            int srcPos = currentIdx + targetLength;
            if (i + 1 == indexListLength && sourceLength != srcPos) {
                currentLength = sourceLength - srcPos;
                buf = new byte[currentLength];
                System.arraycopy(source, srcPos, buf, 0, currentLength);
                bytes.add(buf);
            }
            preIdx = currentIdx + targetLength;
        }
        return bytes;
    }


    /**
     * <p>Adds all the elements of the given arrays into a new array.
     * <p>The new array contains all of the element of {@code array1} followed
     * by all of the elements {@code array2}. When an array is returned, it is always
     * a new array.
     *
     * <pre>
     * ArrayUtils.addAll(array1, null)   = cloned copy of array1
     * ArrayUtils.addAll(null, array2)   = cloned copy of array2
     * ArrayUtils.addAll([], [])         = []
     * </pre>
     *
     * @param array1 the first array whose elements are added to the new array.
     * @param array2 the second array whose elements are added to the new array.
     * @return The new byte[] array.
     * @since 2.1
     */
    public static byte[] addAll(final byte[] array1, final byte... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        final byte[] joinedArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    /**
     * <p>Clones an array returning a typecast result and handling
     * {@code null}.
     *
     * <p>This method returns {@code null} for a {@code null} input array.
     *
     * @param array the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static byte[] clone(final byte[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }
}
