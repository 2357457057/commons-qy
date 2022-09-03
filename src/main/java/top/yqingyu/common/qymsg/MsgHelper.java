package top.yqingyu.common.qymsg;


/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.qymsg.MsgHelper
 * @description
 * @createTime 2022年09月02日 00:31:00
 */
public class MsgHelper {


    public static String gainMsg(QyMsg msg) {
        return msg.getDataMap().getString("MSG", "");
    }

    public static Object gainObjMsg(QyMsg msg) {
        return msg.getDataMap().get("MSG");
    }

    public static String gainMsgValue(QyMsg msg, String key) {
        return msg.getDataMap().getString(key, "");
    }



}
