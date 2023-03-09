package top.yqingyu.common.exception;

/**
 * 服务器异常枚举
 *
 * @author 杨永基
 * date: 2021/09/09
 */
public enum QyExceptionEnum implements QyErrorInfoInterface {
    // 数据操作错误定义
    SUCCESS("200", "success!"),
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
    private String resultCode;

    /**
     * 错误描述
     */
    private String resultMsg;

    public QyExceptionEnum setResultCode(String resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public QyExceptionEnum setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
        return this;
    }

    QyExceptionEnum(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    QyExceptionEnum(String resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }
}
