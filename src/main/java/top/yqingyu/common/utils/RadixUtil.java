package top.yqingyu.common.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class RadixUtil {
    private static final byte[] BYTE_DICT = {
            '.', '<', '>', 'q', 'r', 's', '/', '?', 'E', 'd',
            'e', '{', '}', ';', ':', 'a', 'b', 'c', '0', '1',
            '2', '`', '!', '@', 'Y', 'Z', '~', 'n', 'o', 'O',
            '3', '4', 'f', '#', '$', '%', 'g', 'h', 'i', '5',
            '6', '7', 'T', 'U', 'V', '*', '(', ')', '-', '+',
            '=', 'v', 'w', 'x', 'W', 'X', 'u', 'P', 'Q', 'R',
            'C', 'D', '&', 'F', 'G', 'H', 'k', 'l', 'm', '[',
            ']', 'p', '8', '9', 'J', 'z', 'A', 'B', 'K', 'L',
            'M', ',', 'I', 'j', '^', 'N', 'S', '_', 'y', '|',
            't', '\\', '\'', '\"'
    };
    private static final HashMap<Byte, Integer> BYTE_MAP = new HashMap<>();
    private static final HashMap<Integer, Byte> F_BYTE_MAP = new HashMap<>();

    static {
        for (int i = 0; i < BYTE_DICT.length; i++) {
            BYTE_MAP.put(BYTE_DICT[i], i);
        }
        BYTE_MAP.forEach((b, i) -> F_BYTE_MAP.put(i, b));
    }

    public static int byte2Radix(byte[] num, int radix) {
        checkRadix(radix);
        int length = num.length - 1;
        int total = 0;
        for (byte b : num) {
            int i1 = getRadixNum(b, radix);
            total += (int) (i1 * Math.pow(radix, length));
            length--;
        }
        return total;
    }

    public static byte[] radix2Byte(final int num, int radix) {
        checkRadix(radix);
        ArrayList<Byte> bytes = new ArrayList<>(10);
        int numCp = num;
        do {
            bytes.add(F_BYTE_MAP.get(numCp % radix));
            numCp = numCp / radix;
        } while (numCp != 0);
        byte[] bs = new byte[bytes.size()];
        for (int i = 0; i < bs.length; i++) {
            bs[i] = bytes.get(i);
        }
        return bs;
    }

    public static int getRadixNum(byte b, int radix) {
        Integer i = BYTE_MAP.get(b);
        if (i > radix)
            throw new IllegalArgumentException("超出解析范围");
        return i;
    }

    public static void checkRadix(int radix) {
        if (radix > BYTE_DICT.length)
            throw new IllegalArgumentException("超出最大进制");
    }
}
