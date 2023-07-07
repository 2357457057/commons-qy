package top.yqingyu.common.exception;

import java.io.Serial;

/**
 * 文件创建异常
 *
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.exception.IllegalFileCreateException
 * @description
 * @date 2023年01月04日 18:38:00
 */
public class IllegalFileCreateException extends QyRuntimeException {
    @Serial
    private static final long serialVersionUID = -7251996375253256840L;

    public IllegalFileCreateException() {
    }

    public IllegalFileCreateException(String message, Object... o) {
        super(message, o);
    }

    public IllegalFileCreateException(Throwable cause, String message, Object... o) {
        super(cause, message, o);
    }

    public IllegalFileCreateException(Throwable cause) {
        super(cause);
    }

    public IllegalFileCreateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... o) {
        super(message, cause, enableSuppression, writableStackTrace, o);
    }
}
