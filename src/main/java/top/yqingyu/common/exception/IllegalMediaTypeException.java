package top.yqingyu.common.exception;

import java.io.Serial;

/**
 * 错误的媒体类型
 *
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.exception.IllegalMediaTypeException
 * @description
 * @createTime 2023年01月06日 21:31:00
 */
public class IllegalMediaTypeException extends QyRuntimeException {
    @Serial
    private static final long serialVersionUID = -7733694506076874702L;

    public IllegalMediaTypeException() {
    }

    public IllegalMediaTypeException(String message, Object... o) {
        super(message, o);
    }

    public IllegalMediaTypeException(Throwable cause, String message, Object... o) {
        super(cause, message, o);
    }

    public IllegalMediaTypeException(Throwable cause) {
        super(cause);
    }

    public IllegalMediaTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... o) {
        super(message, cause, enableSuppression, writableStackTrace, o);
    }
}
