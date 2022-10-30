package top.yqingyu.common.nio$server.event$http.compoment;

import cn.hutool.core.lang.UUID;
import top.yqingyu.common.utils.StringUtil;
import top.yqingyu.common.utils.YamlUtil;

import java.io.*;
import java.nio.channels.FileChannel;


/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$http.compoment.MultipartFile
 * @description
 * @createTime 2022年10月27日 18:23:00
 */
public class MultipartFile {
    private String fileName;

    private String originFileName = UUID.randomUUID().toString();

    private final File file;

    private FileChannel fileChannel;
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;

    public MultipartFile(String fileName, String path) throws IOException {
        this.fileName = fileName;

        if (StringUtil.lastIndexOfAny(path, "/", "\\") < 0)
            path = YamlUtil.isWindows() ? (path + "\\") : (path + "/");

        String[] split = fileName.split("[.]");

        if (split.length >= 2)
            originFileName = originFileName + split[split.length - 1];

        file = new File(path + originFileName);

        if (file.createNewFile())
            fileOutputStream = new FileOutputStream(file);


    }

    public String getFileName() {
        return fileName;
    }

    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(file);
    }

    void write(byte[] bytes) throws IOException {
        fileOutputStream.write(bytes);
    }


}
