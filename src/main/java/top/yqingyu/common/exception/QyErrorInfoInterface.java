package top.yqingyu.common.exception;

/**
 * 服务器异常接口
 *
 * @author 杨永基
 * date: 2021/09/09
 */
public interface QyErrorInfoInterface {
    /**
     * 错误码
     */
    String getRspCode();

    /**
     * 错误描述
     */
    String getRspMsg();
}
