package top.yqingyu.common.exception;

import top.yqingyu.common.utils.StringUtil;

public class QyRuntimeException extends RuntimeException{
    public QyRuntimeException() {
    }

    public QyRuntimeException(String message, Object... o) {
        super(StringUtil.fillBrace(message, o));
    }

    public QyRuntimeException(Throwable cause, String message, Object... o) {
        super(StringUtil.fillBrace(message, o), cause);
    }

    public QyRuntimeException(Throwable cause) {
        super(cause);
    }

    public QyRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... o) {
        super(StringUtil.fillBrace(message, o), cause, enableSuppression, writableStackTrace);
    }
}
