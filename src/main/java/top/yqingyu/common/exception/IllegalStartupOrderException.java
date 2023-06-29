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
public class IllegalStartupOrderException extends RuntimeException {

    private static final String prefix = "非法的启动顺序，应先启动：";
    @Serial
    private static final long serialVersionUID = -1161092203853822343L;

    public IllegalStartupOrderException() {
        super();
    }

    public IllegalStartupOrderException(String message) {
        super(prefix + message);
    }

    public IllegalStartupOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalStartupOrderException(Throwable cause) {
        super(cause);
    }

    protected IllegalStartupOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
