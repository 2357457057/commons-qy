package top.yqingyu.common.nio$server.event$http.exception;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$http.exception.HttpException
 * @description
 * @createTime 2022年09月15日 23:14:00
 */
public class HttpException {
    public static class MethodNotSupposedException extends RuntimeException {
        /**
         * Constructs a new runtime exception with the specified detail message.
         * The cause is not initialized, and may subsequently be initialized by a
         * call to {@link #initCause}.
         *
         * @param message the detail message. The detail message is saved for
         *                later retrieval by the {@link #getMessage()} method.
         */
        public MethodNotSupposedException(String message) {
            super(message);
        }
    }
}
