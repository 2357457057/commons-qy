package top.yqingyu.common.bean;

import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;

/**
 * @author YYJ
 * @version 1.0.0
 * @date 2022/4/21 2:11
 * @description
 * @modified by
 */
public record NioFile(String fileName,
                      PosixFilePermission filePermission) implements FileAttribute<PosixFilePermission> {


    public static FileAttribute[] getFileAttributes(String fileName, PosixFilePermission... permission) {


        FileAttribute[] fileAttributes = new FileAttribute[permission.length];

        for (int i = 0; i < permission.length; i++) {
            fileAttributes[i] = new NioFile(fileName, permission[i]);
        }
        return fileAttributes;
    }


    /**
     * Returns the attribute name.
     *
     * @return The attribute name
     */
    @Override
    public String name() {
        return fileName;
    }

    /**
     * Returns the attribute value.
     *
     * @return The attribute value
     */
    @Override
    public PosixFilePermission value() {
        return filePermission;
    }
}
