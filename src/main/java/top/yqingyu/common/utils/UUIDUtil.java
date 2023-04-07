package top.yqingyu.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;


public class UUIDUtil {

    private final long mostSigBits;
    private final long leastSigBits;

    static final char[] digits = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    private UUIDUtil(byte[] data) {
        long msb = 0;
        long lsb = 0;
        assert data.length == 16 : "data must be 16 bytes in length";
        for (int i = 0; i < 8; i++)
            msb = (msb << 8) | (data[i] & 0xff);
        for (int i = 8; i < 16; i++)
            lsb = (lsb << 8) | (data[i] & 0xff);
        this.mostSigBits = msb;
        this.leastSigBits = lsb;
    }

    private static class Holder {
        static final SecureRandom numberGenerator = new SecureRandom();
    }


    public UUIDUtil(long mostSigBits, long leastSigBits) {
        this.mostSigBits = mostSigBits;
        this.leastSigBits = leastSigBits;
    }

    public static UUIDUtil randomUUID() {
        SecureRandom ng = Holder.numberGenerator;

        byte[] randomBytes = new byte[16];
        ng.nextBytes(randomBytes);
        randomBytes[6] &= 0x0f;  /* clear version        */
        randomBytes[6] |= 0x40;  /* set to version 4     */
        randomBytes[8] &= 0x3f;  /* clear variant        */
        randomBytes[8] |= 0x80;  /* set to IETF variant  */
        return new UUIDUtil(randomBytes);
    }

    byte[] fastUUIDBytes(long lsb, long msb) {
        byte[] buf = new byte[36];
        formatUnsignedLong0(lsb, 4, buf, 24, 12);
        formatUnsignedLong0(lsb >>> 48, 4, buf, 19, 4);
        formatUnsignedLong0(msb, 4, buf, 14, 4);
        formatUnsignedLong0(msb >>> 16, 4, buf, 9, 4);
        formatUnsignedLong0(msb >>> 32, 4, buf, 0, 8);
        buf[23] = '-';
        buf[18] = '-';
        buf[13] = '-';
        buf[8] = '-';
        return buf;
    }

    String fastUUID(long lsb, long msb) {
        return new String(fastUUIDBytes(lsb, msb), StandardCharsets.UTF_8);
    }

    byte[] fastUUIDBytes2(long lsb, long msb) {
        byte[] buf = new byte[32];
        formatUnsignedLong0(lsb, 4, buf, 20, 12);
        formatUnsignedLong0(lsb >>> 48, 4, buf, 16, 4);
        formatUnsignedLong0(msb, 4, buf, 12, 4);
        formatUnsignedLong0(msb >>> 16, 4, buf, 8, 4);
        formatUnsignedLong0(msb >>> 32, 4, buf, 0, 8);
        return buf;
    }

    String fastUUID2(long lsb, long msb) {
        return new String(fastUUIDBytes2(lsb, msb), StandardCharsets.UTF_8);
    }

    static void formatUnsignedLong0(long val, int shift, byte[] buf, int offset, int len) {
        int charPos = offset + len;
        int radix = 1 << shift;
        int mask = radix - 1;
        do {
            buf[--charPos] = (byte) digits[((int) val) & mask];
            val >>>= shift;
        } while (charPos > offset);
    }


    @Override
    public String toString() {
        return fastUUID(this.leastSigBits, this.mostSigBits);
    }

    public String toString2() {
        return fastUUID2(this.leastSigBits, this.mostSigBits);
    }
}
