package top.yqingyu.common.server$aio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.CompletionHandler;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.server$aio.ReadHandler
 * @description
 * @createTime 2023年02月10日 16:45:00
 */
public class ReadHandler implements CompletionHandler<Integer, SessionBridge> {

    private final SessionBridge sessionBridge;

    ReadHandler(SessionBridge sessionBridge) {
        this.sessionBridge = sessionBridge;
    }

    @Override
    public void completed(Integer result, SessionBridge attachment) {
        sessionBridge.readLock.countDown();
        sessionBridge.readResult = result;
    }

    @Override
    public void failed(Throwable exc, SessionBridge attachment) {
        sessionBridge.readLock.countDown();
        sessionBridge.readError(exc);
    }
}