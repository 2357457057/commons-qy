package top.yqingyu.common.utils;





import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class DecryptAES {
    final private static SecureRandom random;
    final private static String def_key = "3h09951v11.wicp.vip:37691";
    private Cipher decryptCipher;

    static {
        random = new SecureRandom();
        byte rand3 = getRand3();

    }



    /**
     * 加密key
     *
     * @param key
     * @return
     */
    public static byte[] encryptKeyByte(String key) {

        if (StringUtil.isEmpty(key)) {
            key = def_key;
        }

        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

        return encryptKeyByte(keyBytes);
    }

    /**
     * 加密key
     *
     * @param key
     * @return
     */
    public static byte[] encryptKeyByte(byte[] key) {


        byte count;

        byte randNum;

        byte[] store = new byte[key.length * 3];

        int keyBytes_length = key.length;


        for (int i = 1; i <= keyBytes_length; i++) {
            while (true) {
                count = getRand3();

                randNum = getRand3();

                byte temp = key[i - 1];
                for (int j = 0; j < count; j++) {
                    temp -= randNum;

                }
                if (number(temp) && number(count) && number(randNum)) {
                    key[i - 1] = temp;
                    break;
                }
            }

            store[(i * 3) - 3] = count;
            store[(i * 3) - 2] = key[i - 1];
            store[(i * 3) - 1] = randNum;

        }

        return store;
    }

    /**
     * 加密key2
     *
     * @param key
     * @return
     */
    public static byte[] encryptKeyByte2(byte[] key) {


        byte count;

        byte randNum;

        byte[] store = new byte[key.length * 3];

        int keyBytes_length = key.length;


        for (int i = 1; i <= keyBytes_length; i++) {

            count = getRand4();

            randNum = getRand5();

            byte temp = (byte) (randNum * count);

            store[(i * 3) - 3] = count;
            store[(i * 3) - 2] = (byte) (key[i - 1] - temp);
            store[(i * 3) - 1] = randNum;

        }

        return store;
    }

    /**
     * 加密key2
     *
     * @param value
     * @return
     */
    public static byte[] encryptKeyByte2(Object value) {


        byte[]  key = value.toString().getBytes(StandardCharsets.UTF_8);
        byte count;

        byte randNum;

        byte[] store = new byte[key.length * 3];

        int keyBytes_length = key.length;


        for (int i = 1; i <= keyBytes_length; i++) {

            count = getRand4();

            randNum = getRand5();

            byte temp = (byte) (randNum * count);

            store[(i * 3) - 3] = count;
            store[(i * 3) - 2] = (byte) (key[i - 1] - temp);
            store[(i * 3) - 1] = randNum;

        }

        return store;
    }


    /**
     * 加密key base64
     *
     * @param key
     * @return
     */
    public static byte[] encryptKeyByteBase64(String key) {

        if (StringUtil.isEmpty(key)) {
            key = def_key;
        }

        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

        byte count;

        byte randNum;

        byte[] store = new byte[keyBytes.length * 3];

        int keyBytes_length = keyBytes.length;


        for (int i = 1; i <= keyBytes_length; i++) {
            while (true) {
                count = getRand3();

                randNum = getRand3();

                byte temp = keyBytes[i - 1];
                for (int j = 0; j < count; j++) {
                    temp -= randNum;

                }
                if (number(temp) && number(count) && number(randNum)) {
                    keyBytes[i - 1] = temp;
                    break;
                }
            }

            store[(i * 3) - 3] = count;
            store[(i * 3) - 2] = keyBytes[i - 1];
            store[(i * 3) - 1] = randNum;

        }
        return Base64.getEncoder().encode(store);
    }

    /**
     * 解密key
     *
     * @param encryptKey
     * @return
     */
    public static byte[] decryptKeyByte(String encryptKey) {
        byte[] bytes = encryptKey.getBytes(StandardCharsets.UTF_8);

        return decryptKeyByte(bytes);
    }

    /**
     * 解密key
     *
     * @param bytes
     * @return
     */
    public static byte[] decryptKeyByte(byte[] bytes) {


        byte[] key = new byte[bytes.length / 3];
        byte count;
        byte keyBytes;
        byte randNum;

        for (int i = 1; i <= bytes.length / 3; i++) {
            count = bytes[(i * 3) - 3];
            keyBytes = bytes[(i * 3) - 2];
            randNum = bytes[(i * 3) - 1];

            for (int j = 0; j < count; j++) {
                keyBytes += randNum;
            }

            key[i - 1] = keyBytes;
        }

        return key;
    }

    public static String decryptBase64Key(String encryptKey) {
        byte[] bytes = Base64.getDecoder().decode(encryptKey.getBytes(StandardCharsets.UTF_8));
        return new String(decryptKeyByte(bytes), StandardCharsets.UTF_8);
    }

    /**
     * 去除反斜杠
     *
     * @param b
     * @return
     */
    private static boolean number(byte b) {
        return (b >= 33 && b < 92) || (b > 92 && b <= 126);
    }


    /**
     * AES加密 +base64
     *
     * @param content 需要加密的内容
     * @param keyStr  加密密码
     * @return
     */
    public static byte[] encryptAES(String content, String keyStr) {
        try {
            KeyGenerator kGen = KeyGenerator.getInstance("AES");
            kGen.init(128, new SecureRandom(decryptKeyByte(keyStr)));
            SecretKey secretKey = kGen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();

            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化

            return Base64.getEncoder().encode(cipher.doFinal(content.getBytes(StandardCharsets.UTF_8))); // 加密
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密 + Base64
     *
     * @param content 待解密内容
     * @param keyStr  解密密钥
     * @return
     */
    public static byte[] decryptAES(byte[] content, String keyStr) {
        try {
            KeyGenerator kGen = KeyGenerator.getInstance("AES");
            kGen.init(128, new SecureRandom(decryptKeyByte(keyStr)));
            SecretKey secretKey = kGen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();

            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化


            return cipher.doFinal(Base64.getDecoder().decode(content)); // 解密
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte getRand() {
        int i = random.nextInt(128);
        int j = random.nextInt(2);
        if (j == 0) i = -i;
        return (byte) i;
    }


    private static byte getRand2() {
        int i = random.nextInt(94);
        i += 33;
        return (byte) i;
    }

    private static byte getRand3() {
        byte i;
        do {
            i = (byte) random.nextInt(94);
            i += 33;
        } while (!number(i));
        return i;
    }

    private static byte getRand4() {
        byte i;

        i = (byte) random.nextInt(128);

        return i;
    }

    private static byte getRand5() {
        byte i;

        i = (byte) random.nextInt(256);

        return i;
    }

}
