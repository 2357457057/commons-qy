package top.yqingyu.common.qymsg;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.yqingyu.common.qydata.DataMap;

import java.io.Serializable;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.qymsg.QyMessage
 * @description
 * @createTime 2022年07月12日 01:13:00
 */
@Data
@AllArgsConstructor
@SuppressWarnings("all")
public class QyMsg implements Serializable, Cloneable {
    private final static long serialVersionUID = -1854823182151532168L;
    private final MsgType msgType;
    private DataType dataType;

    //来自谁的消息
    private String from;

    //发给谁的消息
    private String to;

    //是否分区
    private boolean segmentation;

    //碎片ID
    private String partition_id;

    private Integer numerator;
    //分片
    private Integer denominator;
    private DataMap dataMap = new DataMap(); //具体消息

    public QyMsg(MsgType msgType, DataType dataType) {
        this.msgType = msgType;
        this.dataType = dataType;
    }

    public void putMsg(String msg) {
        this.dataMap.put("MSG", msg);
    }

    public void putMsg(Object msg) {
        this.dataMap.put("MSG", msg);

    }

    public QyMsg putMsgData(String msgKey, String msgValue) {
        this.dataMap.put(msgKey, msgValue);
        return this;
    }

    public QyMsg putMsgData(String msgKey, Object msgValue) {
        this.dataMap.put(msgKey, msgValue);
        return this;
    }

    @Override
    public QyMsg clone() throws CloneNotSupportedException {
        QyMsg clone = (QyMsg) super.clone();
        DataMap dataMap = clone.dataMap.clone();
        clone.setDataMap(dataMap);
        return clone;
    }
}
