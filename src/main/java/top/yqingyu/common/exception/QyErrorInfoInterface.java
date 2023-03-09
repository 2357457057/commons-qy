package top.yqingyu.common.exception;

/**
 * 服务器异常接口
 *
 * @author 杨永基
 * date: 2021/09/09
 */
interface QyErrorInfoInterface {
    /**
     * 错误码
     */
    String getResultCode();

    /**
     * 错误描述
     */
    String getResultMsg();
}
