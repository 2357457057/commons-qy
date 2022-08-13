package top.yqingyu.common.exception;

/**
  *description:
  *@author 杨永基
  *date: 2021/09/09
  */
public enum QyExceptionEnum implements QyErrorInfoInterface {
    // 数据操作错误定义
    SUCCESS("200", "成功!"),
    BODY_NOT_MATCH("400","请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH("401","请求的数字签名不匹配!"),
    REQUEST_MOTHERED_NOT_SUPPORTED("500","请求方法不支持！"),
    NOT_FOUND("404", "未找到该资源!"),
    INTERNAL_SERVER_ERROR("500", "服务器内部错误!"),
    SERVER_BUSY("503","服务器正忙，请稍后再试!"),
    ERROR_400("400"),
    ERROR_500("500"),
    SIGNATURE_NOT_EXIST("401","数字签名不存在!"),
    SIGN_TIMEOUT("401","由于超时未操作，身份已过期，请重新登录！"),
    SIGNATURE_TOKEN_IS_EMPTY("401","请求头未包含token!"),
    USER_INFO_ALlREADY_IN_USE("402","用户名或邮箱已被注册")
    ;



    /** 错误码 */
    private String resultCode;

    /** 错误描述 */
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
