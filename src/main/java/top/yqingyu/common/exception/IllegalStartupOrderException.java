package top.yqingyu.common.exception;

import java.io.Serial;

/**
 * 启动顺序异常
 *
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.exception.IllegalStartupOrderException
 * @description
 * @createTime 2023年02月09日 22:20:00
 */
public class IllegalStartupOrderException extends QyRuntimeException {

    private static final String prefix = "非法的启动顺序，应先启动：";
    @Serial
    private static final long serialVersionUID = -1161092203853822343L;

    public IllegalStartupOrderException() {
    }

    public IllegalStartupOrderException(String message, Object... o) {
        super(prefix + message, o);
    }

    public IllegalStartupOrderException(Throwable cause, String message, Object... o) {
        super(cause, prefix + message, o);
    }

    public IllegalStartupOrderException(Throwable cause) {
        super(cause);
    }

    public IllegalStartupOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... o) {
        super(prefix + message, cause, enableSuppression, writableStackTrace, o);
    }
}
