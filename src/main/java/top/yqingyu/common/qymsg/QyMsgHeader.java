package top.yqingyu.common.qymsg;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@SuppressWarnings("all")
public final class QyMsgHeader implements Serializable, Cloneable {
    private Integer msgType;  // 2  bytes, 1 Object , 0 String
    private String from; //来自谁的消息
    private String to;  //发给谁的消息
    private DataMap dataMap = new DataMap(); //具体消息


    public void putMsg(String msg) {
        this.dataMap.put("MSG", msg);
    }

    public void putMsg(Object msg) {
        this.dataMap.put("MSG", msg);

    }

    public QyMsgHeader putMsgData(String msgKey, String msgValue) {
        this.dataMap.put(msgKey, msgValue);
        return this;
    }

    public QyMsgHeader putMsgData(String msgKey, Object msgValue) {
        this.dataMap.put(msgKey, msgValue);
        return this;
    }

    public String gainMsg() {
        return this.dataMap.getString("MSG", "");
    }

    public Object gainObjMsg() {
        return this.dataMap.get("MSG");
    }

    public DataMap gainMsgData() {
        return this.dataMap;
    }


    public String gainMsgValue(String key) {
        return this.dataMap.getString(key, "");
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override
    public QyMsgHeader clone() throws CloneNotSupportedException {
        QyMsgHeader clone = (QyMsgHeader) super.clone();
        DataMap dataMap = clone.dataMap.clone();
        clone.setDataMap(dataMap);
        return clone;
    }


    public static class MsgType {


        public static Integer AC = 1 << 2;             // 认证消息
        public static Integer HEART_BEAT = 1 << 4;     //心跳
        public static Integer NORM_MSG = 1 << 8; //普通消息
        public static Integer ERR_MSG = 1 << 6; //普通消息


    }
}
