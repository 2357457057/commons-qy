package top.yqingyu.common.exception;

import java.io.Serial;

/**
 * 服务器异常
 *
 * @author 杨永基
 * date: 2021/09/09
 */
public class QyException extends RuntimeException implements QyErrorInfoInterface{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected String errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public QyException() {
        super();
    }

    public QyException(QyErrorInfoInterface errorInfoInterface) {
        super(errorInfoInterface.getRspMsg());
        this.errorCode = errorInfoInterface.getRspCode();
        this.errorMsg = errorInfoInterface.getRspMsg();
    }

    public QyException(QyErrorInfoInterface errorInfoInterface, Throwable cause) {
        super(errorInfoInterface.getRspMsg(), cause);
        this.errorCode = errorInfoInterface.getRspCode();
        this.errorMsg = errorInfoInterface.getRspMsg();
    }

    public QyException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public QyException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public QyException(String errorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


    public String getErrorCode() {
        return errorCode;
    }

    public QyException setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public QyException setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public String getMessage() {
        return errorMsg;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public String getRspCode() {
        return errorCode;
    }

    @Override
    public String getRspMsg() {
        return errorMsg;
    }
}
