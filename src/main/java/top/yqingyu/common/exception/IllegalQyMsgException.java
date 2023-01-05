package top.yqingyu.common.exception;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.exception.IllegalQyMsgException
 * @description
 * @createTime 2023年01月05日 22:36:00
 */
public class IllegalQyMsgException extends RuntimeException{
    public IllegalQyMsgException() {
        super();
    }
    public IllegalQyMsgException(String message) {
        super(message);
    }
    public IllegalQyMsgException(String message, Throwable cause) {
        super(message, cause);
    }
    public IllegalQyMsgException(Throwable cause) {
        super(cause);
    }

    protected IllegalQyMsgException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
