package top.yqingyu.common.qymsg;

import com.alibaba.fastjson2.JSON;
import top.yqingyu.common.qydata.DataMap;
import top.yqingyu.common.utils.IoUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        QyMsg qyMsg = new QyMsg(MsgType.NORM_MSG, DataType.JSON);
        qyMsg.setFrom("小杨");
        qyMsg.setTo("小苏");
        qyMsg.putMsg("我爱你");

        byte[] bytes = IoUtil.objToSerializBytes(qyMsg);
        QyMsg qyMsg1 = IoUtil.deserializationObj(bytes, QyMsg.class);
        System.out.println(qyMsg1);

    }

}
