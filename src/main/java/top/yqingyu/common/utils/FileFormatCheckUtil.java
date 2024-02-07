package top.yqingyu.common.utils;


import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

/**
 * 文件格式校验 通过文件内容特征码校验。
 */
public class FileFormatCheckUtil {

    public static class FileType {
        private final Checker[] CHECK;
        private final int maxLength;

        public FileType(FileType imageType) {
            this.CHECK = imageType.CHECK;
            this.maxLength = imageType.maxLength;
        }

        public FileType(Checker... check) {
            CHECK = check;
            int max = 0;
            for (Checker checker : check) {
                max = Math.max(max, checker.content.length);
            }
            maxLength = max;
        }
    }

    public static class ImageType extends FileType {
        public static final ImageType JPEG = new ImageType(new Checker(0, 0xFF, 0xD8), new Checker(-2L, 0xFF, 0xD9));
        public static final ImageType JPG = JPEG;
        public static final ImageType PNG = new ImageType(new Checker(0, 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A));
        public static final ImageType GIF_89a = new ImageType(new Checker(0, 0x47, 0x49, 0x46, 0x38, 0x39, 0x61));
        public static final ImageType GIF_87a = new ImageType(new Checker(0, 0x47, 0x49, 0x46, 0x38, 0x37, 0x61));
        public static final ImageType BMP = new ImageType(new Checker(0, 0x42, 0x4D));
        public static final ImageType TIFF = new ImageType(new Checker(0, 0x49, 0x49, 0x2A, 0x00));
        public static final ImageType TIF = new ImageType(new Checker(0, 0, 0x4D, 0x4D, 0x00, 0x2A));
        public static final ImageType WEBP = new ImageType(new Checker(0, 0x52, 0x49, 0x46, 0x46));
        public static final ImageType SVG = new ImageType(new Checker(0, 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D));
        public static final ImageType ICON = new ImageType(new Checker(0, 0x00, 0x00, 0x01, 0x00));

        public ImageType(Checker... check) {
            super(check);
        }
    }

    public static class Checker {
        //检查起始处 从尾部校验则为负值
        final long offset;
        //检查内容
        final byte[] content;

        public Checker(long offset, int... content) {
            this.offset = offset;
            this.content = new byte[content.length];
            for (int i = 0; i < content.length; i++) {
                this.content[i] = (byte) content[i];
            }
        }
    }

    public static boolean check(Path path, FileType fileType) throws IOException {
        return check0(path, fileType.maxLength, fileType.CHECK);
    }

    /**
     * @param path     文件路径
     * @param checkers 检查元素
     */
    public static boolean check(Path path, Checker... checkers) throws IOException {
        int max = 0;
        for (Checker checker : checkers) {
            max = Math.max(max, checker.content.length);
        }
        return check0(path, max, checkers);
    }

    /**
     * 校验
     *
     * @param path             文件
     * @param maxContentLength 检查元素中最大的长度
     * @param checkers         检查元素
     * @return 通过与否
     */
    public static boolean check0(Path path, int maxContentLength, Checker... checkers) throws IOException {
        long size = Files.size(path);
        ByteBuffer buffer = null;
        try (FileChannel open = FileChannel.open(path, StandardOpenOption.READ)) {
            buffer = ByteBuffer.allocate(maxContentLength);
            for (Checker checker : checkers) {
                open.read(buffer, checker.offset < 0 ? size + checker.offset : checker.offset);
                buffer.flip();
                byte[] headBuf = new byte[checker.content.length];
                buffer.get(headBuf);
                if (!Arrays.equals(headBuf, checker.content)) {
                    return false;
                }
                buffer.clear();
            }
        } finally {
            if (buffer != null) {
                buffer.clear();
            }
        }
        return true;
    }

}
