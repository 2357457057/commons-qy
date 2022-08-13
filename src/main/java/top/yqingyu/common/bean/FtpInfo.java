package top.yqingyu.common.bean;



//import org.springframework.boot.context.properties.ConfigurationProperties;
//
//import org.springframework.stereotype.Component;

/**
 * @author YYJ
 * @version 1.0.0
 * @date 2022/3/19 13:26
 * @description
 * @modified by
 */

//@ConfigurationProperties(prefix = "sftp" ,ignoreUnknownFields = true)
//@Component
public class FtpInfo {

    private String ip;

    private String port;

    private String username;

    private String pwd;
    private String privateKey;

    public FtpInfo() {
    }

    public FtpInfo(String ip, String port, String username, String pwd) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.pwd = pwd;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
