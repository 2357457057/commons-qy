package top.yqingyu.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import top.yqingyu.common.response.R;


/**
 * description:
 *
 * @author 杨永基
 * date: 2021/09/09
 */
@Slf4j
@RestControllerAdvice
public class QyGlobalExceptionHandler implements ErrorController {

    private final static Logger logger = LoggerFactory.getLogger(QyGlobalExceptionHandler.class);

    /**
     * 处理自定义的业务异常
     *
     * @param rsp
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = QyException.class)
    public R globalExceptionHandler(HttpServletResponse rsp, QyException e) {
        logger.error("发生业务异常！原因是：{}", e.getErrorMsg());
//        rsp.setStatus(Integer.parseInt(ExceptionEnum.BODY_NOT_MATCH.getResultCode()));
        rsp.setStatus(200);
        return R.error(e.getErrorCode(), e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     *
     * @param rsp
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = NullPointerException.class)
    public R globalExceptionHandler(HttpServletResponse rsp, NullPointerException e) {
        logger.error("发生空指针异常！原因是:", e);
//        rsp.setStatus(Integer.parseInt(ExceptionEnum.BODY_NOT_MATCH.getResultCode()));
        return R.error(QyExceptionEnum.BODY_NOT_MATCH);
    }

    /**
     * 处理空指针的异常
     *
     * @param rsp
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public R globalExceptionHandler(HttpServletResponse rsp, HttpRequestMethodNotSupportedException e) {
        logger.error("请求的方法不匹配:", e);
//        rsp.setStatus(Integer.parseInt(ExceptionEnum.BODY_NOT_MATCH.getResultCode()));
        return R.error(QyExceptionEnum.REQUEST_MOTHERED_NOT_SUPPORTED);
    }




    /**
     * 处理其他异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public R globalExceptionHandler(HttpServletRequest req, Exception e, HttpServletResponse rsp) {

        if (e instanceof NoHandlerFoundException) {
            logger.error("404 >>" + req.getServletPath() + "不存在", e);
//            rsp.setStatus(404);
            return R.error(QyExceptionEnum.NOT_FOUND.setResultMsg("访问的资源>>[ " + req.getServletPath() + " ]<<不存在"));
        }
        rsp.setStatus(500);
        logger.error("未知异常！原因是:", e);
        return R.error(QyExceptionEnum.INTERNAL_SERVER_ERROR);
    }
}
