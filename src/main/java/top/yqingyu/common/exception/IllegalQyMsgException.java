package top.yqingyu.common.exception;

import java.io.Serial;

/**
 * QyMsg 的部分异常
 *
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.exception.IllegalQyMsgException
 * @description
 * @createTime 2023年01月05日 22:36:00
 */
public class IllegalQyMsgException extends QyRuntimeException {
    @Serial
    private static final long serialVersionUID = -723333772533793164L;

    public IllegalQyMsgException() {
    }

    public IllegalQyMsgException(String message, Object... o) {
        super(message, o);
    }

    public IllegalQyMsgException(Throwable cause, String message, Object... o) {
        super(cause, message, o);
    }

    public IllegalQyMsgException(Throwable cause) {
        super(cause);
    }

    public IllegalQyMsgException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... o) {
        super(message, cause, enableSuppression, writableStackTrace, o);
    }
}
