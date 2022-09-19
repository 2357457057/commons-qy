package top.yqingyu.common.nio$server.core;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.core.ExceedingRepetitionLimitException
 * @description 为解决Epoll 问题而生
 * @createTime 2022年09月20日 00:24:00
 */
public class ExceedingRepetitionLimitException extends RuntimeException{
    /**
     * @param message 错误信息
     */
    public ExceedingRepetitionLimitException(String message) {
        super(message);
    }
}
