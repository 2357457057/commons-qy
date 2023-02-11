package top.yqingyu.common.server$aio;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.server$aio.AcceptHandler
 * @description
 * @createTime 2023年02月10日 16:46:00
 */
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, SessionBridge> {

    private final SessionBridge sessionBridge;

    public AcceptHandler(SessionBridge sessionBridge) {
        this.sessionBridge = sessionBridge;
    }

    @Override
    public void completed(AsynchronousSocketChannel result, SessionBridge attachment) {
        sessionBridge.ready(result);
    }

    @Override
    public void failed(Throwable exc, SessionBridge attachment) {
        sessionBridge.session.doLogicError(exc);
    }
}