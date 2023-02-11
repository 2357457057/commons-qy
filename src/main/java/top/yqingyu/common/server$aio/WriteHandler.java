package top.yqingyu.common.server$aio;

import java.nio.channels.CompletionHandler;

public class WriteHandler implements CompletionHandler<Integer, SessionBridge> {

    private final SessionBridge sessionBridge;

    WriteHandler(SessionBridge sessionBridge) {
        this.sessionBridge = sessionBridge;
    }

    @Override
    public void completed(Integer result, SessionBridge attachment) {
        sessionBridge.writeLock.countDown();
        sessionBridge.writeResult = result;
    }

    @Override
    public void failed(Throwable exc, SessionBridge attachment) {
        sessionBridge.writeLock.countDown();
        sessionBridge.writeError(exc);
    }
}