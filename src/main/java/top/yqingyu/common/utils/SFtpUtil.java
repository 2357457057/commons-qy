//package top.yqingyu.common.utils;
//
//
//import com.jcraft.jsch.*;
//import org.apache.commons.io.IOUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import top.yqingyu.common.bean.FtpInfo;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.util.Properties;
//import java.util.Vector;
//
///**
// * @author YYJ
// * @version 1.0.0
// * @date 2022/3/19 16:36
// * @description
// * @modified by
// */
//
//public class SFtpUtil {
//
//    private static final Logger log = LoggerFactory.getLogger(FtpUtil.class);
//
//    private ChannelSftp sftp;
//    private Session session;
//
//
//    public SFtpUtil() {
//    }
//
//
//    /**
//     * 连接sftp服务器
//     */
//    public void login(FtpInfo ftpInfo) throws Exception {
//        try {
//            JSch jsch = new JSch();
//            if (ftpInfo.getPrivateKey() != null) {
//                jsch.addIdentity(ftpInfo.getPrivateKey());// 设置私钥
//            }
//
//            session = jsch.getSession(new String(DecryptAES.decryptKeyByte(ftpInfo.getUsername()), StandardCharsets.UTF_8), ftpInfo.getIp(), Integer.parseInt(ftpInfo.getPort()));
//
//            if (ftpInfo.getPwd() != null) {
//                session.setPassword(new String(DecryptAES.decryptKeyByte(ftpInfo.getPwd()), StandardCharsets.UTF_8));
//            }
//            Properties config = new Properties();
//            config.put("StrictHostKeyChecking", "no");
//
//            session.setConfig(config);
//            session.connect();
//
//            Channel channel = session.openChannel("sftp");
//            channel.connect();
//
//            sftp = (ChannelSftp) channel;
//            log.info("登陆成功！" + ftpInfo.toString());
//        } catch (JSchException e) {
//            log.error("登陆失败{} {}", ftpInfo.toString(), e);
//            throw new Exception("sftp登陆失败",e);
//        }
//    }
//
//    /**
//     * 关闭连接 server
//     */
//    public void logout() {
//        if (sftp != null) {
//            if (sftp.isConnected()) {
//                sftp.disconnect();
//                sftp = null;
//            }
//        }
//        if (session != null) {
//            if (session.isConnected()) {
//                session.disconnect();
//                session = null;
//            }
//        }
//    }
//
//
//    /**
//     * 将输入流的数据上传到sftp作为文件。文件完整路径=basePath+directory
//     *
//     * @param remotePath     上传到该目录
//     * @param remoteBasePath    服务器的基础路径
//     * @param localPath     本地路径
//     * @param sftpFileName         文件名
//     */
//    public void upload(FtpInfo ftpInfo, String remotePath, String remoteBasePath, String sftpFileName, String localPath) throws Exception {
//
//            if (sftp == null) {
//                this.login(ftpInfo);
//            }
//            try {
//                log.info("进入目录: 远程地址: {} 文件名:  {}",remotePath,sftpFileName );
//                sftp.cd(remotePath);
//            } catch (SftpException e) {
//                //目录不存在，则创建文件夹
//                String[] dirs = remotePath.split("/");
//                String tempPath = remoteBasePath;
//                for (String dir : dirs) {
//                    if (null == dir || "".equals(dir)) continue;
//                    tempPath += "/" + dir;
//                    try {
//                        sftp.cd(tempPath);
//                    } catch (SftpException ex) {
//                        sftp.mkdir(tempPath);
//                        sftp.cd(tempPath);
//                    }
//                }
//            }
//            File file = new File(localPath + sftpFileName);
//
//            if (file.exists()) {
//
//                FileInputStream in = new FileInputStream(file);
//
//                long l = System.currentTimeMillis();
//                log.info("文件开始上传 {}", sftpFileName);
//                sftp.put(in, sftpFileName);  //上传文件
//                long l1 = System.currentTimeMillis();
//                log.info("文件上传完毕 {} 耗时{}", sftpFileName, l1 - l);
//
//                this.logout();
//
//            } else {
//                throw new Exception("文件不存在");
//            }
//
//
//
//    }
//
//
//    /**
//     * 下载文件。
//     *
//     * @param directory    下载目录
//     * @param downloadFile 下载的文件
//     * @param saveFile     存在本地的路径
//     */
//    public void download(FtpInfo ftpInfo, String directory, String downloadFile, String saveFile) throws Exception {
//        if (sftp == null) {
//            this.login(ftpInfo);
//        }
//        if (directory != null && !"".equals(directory)) {
//            sftp.cd(directory);
//        }
//        File file = new File(saveFile);
//        sftp.get(downloadFile, new FileOutputStream(file));
//        this.logout();
//    }
//
//    /**
//     * 下载文件
//     *
//     * @param directory    下载目录
//     * @param downloadFile 下载的文件名
//     * @return 字节数组
//     */
//    public byte[] download(String directory, String downloadFile) throws SftpException, IOException {
//        if (directory != null && !"".equals(directory)) {
//            sftp.cd(directory);
//        }
//        InputStream is = sftp.get(downloadFile);
//
//        byte[] fileData = IOUtils.toByteArray(is);
//
//        return fileData;
//    }
//
//
//    /**
//     * 删除文件
//     *
//     * @param directory  要删除文件所在目录
//     * @param deleteFile 要删除的文件
//     */
//    public void delete(FtpInfo ftpInfo, String directory, String deleteFile) throws Exception {
//        this.login(ftpInfo);
//        sftp.cd(directory);
//        sftp.rm(deleteFile);
//        this.logout();
//    }
//
//
//    /**
//     * 列出目录下的文件
//     *
//     * @param directory 要列出的目录
//     */
//    public Vector<?> listFiles(String directory) throws SftpException {
//        return sftp.ls(directory);
//    }
//}
