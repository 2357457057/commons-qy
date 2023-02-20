package top.yqingyu.common.qymsg.extra.bean;

import java.io.Serial;
import java.io.Serializable;

public class TransObj implements Serializable {
    @Serial
    private static final long serialVersionUID = 704921303243464801L;
    private String fileName;
    private final String fileId;
    private long size;
    private boolean isUpload = false;
    private String sendPath;
    private String savePath;
    private boolean hasNext = false;
    private String nextId;

    public TransObj(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    public String getSendPath() {
        return sendPath;
    }

    public void setSendPath(String sendPath) {
        this.sendPath = sendPath;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getFileId() {
        return fileId;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public String getNextId() {
        return nextId;
    }

    public void setNextId(String nextId) {
        this.hasNext = true;
        this.nextId = nextId;
    }
}
