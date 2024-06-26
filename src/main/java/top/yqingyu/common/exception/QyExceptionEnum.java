package top.yqingyu.common.exception;

/**
 * 服务器异常枚举
 *
 * @author 杨永基
 * date: 2021/09/09
 */
public enum QyExceptionEnum implements QyErrorInfoInterface {
    // 数据操作错误定义
    BODY_NOT_MATCH("400", "request data not match!"),
    SIGNATURE_NOT_MATCH("401", "sign not match!"),
    REQUEST_MOTHERED_NOT_SUPPORTED("500", "method not supposed！"),
    NOT_FOUND("404", "not resources"),
    INTERNAL_SERVER_ERROR("500", "server inner error!"),
    SERVER_BUSY("503", "server busi!"),
    ERROR_400("400"),
    ERROR_500("500"),
    SIGNATURE_NOT_EXIST("401", "sign not exists!"),
    SIGN_TIMEOUT("401", "Certification has expired,please login again"),
    SIGNATURE_TOKEN_IS_EMPTY("401", "Header does not contain token"),
    USER_INFO_ALlREADY_IN_USE("402", "username or email address is already registered");


    /**
     * 错误码
     */
    private String rspCode;

    /**
     * 错误描述
     */
    private String rspMsg;

    public QyExceptionEnum setRspCode(String rspCode) {
        this.rspCode = rspCode;
        return this;
    }

    public QyExceptionEnum setRspMsg(String rspMsg) {
        this.rspMsg = rspMsg;
        return this;
    }

    QyExceptionEnum(String rspCode, String rspMsg) {
        this.rspCode = rspCode;
        this.rspMsg = rspMsg;
    }

    QyExceptionEnum(String rspCode) {
        this.rspCode = rspCode;
    }

    @Override
    public String getRspCode() {
        return rspCode;
    }

    @Override
    public String getRspMsg() {
        return rspMsg;
    }
}
