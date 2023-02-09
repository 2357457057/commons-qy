package top.yqingyu.common.server$nio.core;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.core.RebuildSelectorException
 * @description 为解决Epoll 问题而生
 * @createTime 2022年09月20日 00:24:00
 */
public class RebuildSelectorException extends RuntimeException{
    /**
     * @param message 错误信息
     */
    public RebuildSelectorException(String message) {
        super(message);
    }
}
