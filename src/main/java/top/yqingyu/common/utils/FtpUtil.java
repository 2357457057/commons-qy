package top.yqingyu.common.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import top.yqingyu.common.bean.FtpInfo;


import java.io.*;

/**
 * @author YYJ
 * @version 1.0.0
 * @date 2022/3/19 13:24
 * @description
 * @modified by
 */
@Slf4j
public class FtpUtil {



    /**
     * 设置缓冲区大小4M
     **/
    private static final int BUFFER_SIZE = 1024 * 1024 * 4;

    /**
     * 本地字符编码
     **/
    private static String LOCAL_CHARSET = "GBK";

    /**
     * UTF-8字符编码
     **/
    private static final String CHARSET_UTF8 = "UTF-8";

    /**
     * OPTS UTF8字符串常量
     **/
    private static final String OPTS_UTF8 = "OPTS UTF8";

    /**
     * FTP协议里面，规定文件名编码为iso-8859-1
     **/
    private static final String SERVER_CHARSET = "ISO-8859-1";

    private static FTPClient ftpClient = null;

    /**
     * 连接FTP服务器
     */
    private static void login(FtpInfo ftpInfo) {
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpInfo.getIp(), Integer.parseInt(ftpInfo.getPort()));
            ftpClient.login(ftpInfo.getUsername(), ftpInfo.getPwd());
            ftpClient.enterLocalPassiveMode();
            ftpClient.setBufferSize(BUFFER_SIZE);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                closeConnect();
            }
        } catch (Exception e) {
            log.error("登陆失败{} {}",ftpInfo, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭FTP连接
     */
    private static void closeConnect() {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                log.error("", e);
            }
        }
    }

    /**
     * FTP服务器路径编码转换
     *
     * @param ftpPath FTP服务器路径
     * @return String
     */
    private static String changeEncoding(String ftpPath) {
        String directory = null;
        try {
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand(OPTS_UTF8, "ON"))) {
                LOCAL_CHARSET = CHARSET_UTF8;
            }
            directory = new String(ftpPath.getBytes(LOCAL_CHARSET), SERVER_CHARSET);
        } catch (Exception e) {
            log.error("", e);
        }
        return directory;
    }

    /**
     * 改变工作目录
     * 如果没有，则创建工作目录
     *
     * @param path
     */
    private static void changeAndMakeWorkingDir(String path) {
        try {
            ftpClient.changeWorkingDirectory("/");
            path = path.replaceAll("\\\\", "/");
            String[] path_array = path.split("/");
            for (String s : path_array) {
                boolean b = ftpClient.changeWorkingDirectory(s);
                if (!b) {
                    ftpClient.makeDirectory(s);
                    ftpClient.changeWorkingDirectory(s);
                }
            }
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传
     *
     * @return
     */
    public static boolean upload(FtpInfo ftpInfo, String filename, String remotePath, String localPath) {
        login(ftpInfo);
        if (!ftpClient.isConnected()) {
            return false;
        }
        boolean isSuccess = false;

        if (!localPath.endsWith("/")) {
            localPath += "/";
        }

        File file = new File(localPath + filename);
        if (file.exists()) {


            if (ftpClient != null) {
                try {
                    FileInputStream inputStream = new FileInputStream(file);
                    if (FTPReply.isPositiveCompletion(ftpClient.sendCommand(OPTS_UTF8, "ON"))) {
                        LOCAL_CHARSET = CHARSET_UTF8;
                    }
                    ftpClient.setControlEncoding(LOCAL_CHARSET);
                    String path = changeEncoding(remotePath);

                    changeAndMakeWorkingDir(path);
                    isSuccess = ftpClient.storeFile(new String(filename.getBytes(), SERVER_CHARSET), inputStream);
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    closeConnect();
                }
            }
        } else {

            log.error("本地文件不存在 path: {} ,filename: {}", localPath, filename);
        }
        return isSuccess;
    }

    /**
     * 下载
     *
     * @param ftpInfo
     * @param filename
     * @param dirPath
     * @param out
     * @return
     */
    public static void download(FtpInfo ftpInfo, String filename, String dirPath, FileOutputStream out) {
        // 登录
        login(ftpInfo);
        if (ftpClient != null) {
            try {
                String path = changeEncoding(dirPath);
                changeAndMakeWorkingDir(path);
                String[] fileNames = ftpClient.listNames();
                if (fileNames == null || fileNames.length == 0) {
                    return;
                }
                for (String fileName : fileNames) {
                    String ftpName = new String(fileName.getBytes(SERVER_CHARSET), LOCAL_CHARSET);
                    if (StringUtils.equals(ftpName, filename)) {
                        InputStream in = ftpClient.retrieveFileStream(fileName);
                        IOUtils.copy(in, out);
                    }
                }
            } catch (IOException e) {
                log.error("", e);
            } finally {
                closeConnect();
            }
        }
    }
}

