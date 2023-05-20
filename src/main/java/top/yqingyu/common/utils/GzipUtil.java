package top.yqingyu.common.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.utils.GzipUtil
 * @description
 * @createTime 2022年09月18日 01:46:00
 */
public class GzipUtil {

    /**
     * 压缩字符串
     */
    public static byte[] $2CompressBytes(String str, Charset charset) throws IOException {
        if (null == str || str.length() <= 0) {
            return ArrayUtil.EMPTY_BYTE_ARRAY;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            // 将字符串以字节的形式写入到 GZIP 压缩输出流中
            gzip.write(str.getBytes(charset));
            gzip.close();
            return out.toByteArray();
        }
    }

    /**
     * 压缩文件
     */
    public static byte[] $2CompressBytes(File file) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); GZIPOutputStream gzip = new GZIPOutputStream(out); FileInputStream in = new FileInputStream(file)) {
            // 将文件以字节的形式写入到 GZIP 压缩输出流中
            gzip.write(in.readAllBytes());
            gzip.close();
            return out.toByteArray();
        }
    }


    /**
     * 压缩字符串
     */
    public static String compress(String str) {
        if (null == str || str.length() <= 0) {
            return str;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); GZIPOutputStream gzip = new GZIPOutputStream(out)) { // 将字符串以字节的形式写入到 GZIP 压缩输出流中
            gzip.write(str.getBytes(StandardCharsets.UTF_8));
            gzip.close();
            return out.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * 解压缩字符串
     */
    public static String decompress(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)); GZIPInputStream gunzip = new GZIPInputStream(in)) {
            byte[] buffer = new byte[256];
            int n;
            // 从 GZIP 压缩输入流读取字节数据到 buffer 数组中
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}
