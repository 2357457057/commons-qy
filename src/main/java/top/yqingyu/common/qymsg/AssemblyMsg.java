package top.yqingyu.common.qymsg;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import top.yqingyu.common.utils.ArrayUtil;
import top.yqingyu.common.utils.IoUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.qymsg.AssemblyMsg
 * @description
 * @createTime 2022年09月06日 10:36:00
 */
@Slf4j
 class AssemblyMsg {


    /**
     * @param qyMsg 消息
     * @author YYJ
     * @description 消息组装
     */
    public static ArrayList<byte[]> assembly(QyMsg qyMsg) throws IOException {
        ArrayList<byte[]> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(MsgTransfer.MSG_TYPE_2_CHAR(qyMsg.getMsgType()));
        sb.append(MsgTransfer.DATA_TYPE_2_CHAR(qyMsg.getDataType()));

        if (MsgType.AC.equals(qyMsg.getMsgType())) {

            //认证消息组装
            AC_Assembly(sb, qyMsg, list);
        } else if (MsgType.HEART_BEAT.equals(qyMsg.getMsgType())) {

            //心跳消息组装
            HEART_BEAT_Assembly(sb, qyMsg, list);
        } else if (MsgType.NORM_MSG.equals(qyMsg.getMsgType())) {

            //普通消息组装
            NORM_MSG_Assembly(sb, qyMsg, list);
        } else if (MsgType.ERR_MSG.equals(qyMsg.getMsgType())) {

            ERR_MSG_Assembly(sb, qyMsg, list);
        } else {

            //普通消息组装
            NORM_MSG_Assembly(sb, qyMsg, list);
        }
        //将信息长度与信息组合
        return list;
    }


    /**
     * 认证消息组装
     *
     * @param qyMsg 消息体对象
     * @param sb    消息头SB
     * @param list  返回的消息集合
     */
    private static void AC_Assembly(StringBuilder sb, QyMsg qyMsg, ArrayList<byte[]> list) {
        sb.setCharAt(1, MsgTransfer.DATA_TYPE_2_CHAR(DataType.JSON));
        sb.append(MsgTransfer.BOOLEAN_2_SEGMENTATION(false));
        byte[] body = JSON.toJSONBytes(qyMsg);
        sb.append(MsgTransfer.getLength(body));
        byte[] header = sb.toString().getBytes(StandardCharsets.UTF_8);
        list.add(ArrayUtils.addAll(header, body));
    }

    /**
     * 心跳消息组装
     *
     * @param qyMsg 消息体对象
     * @param sb    消息头SB
     * @param list  返回的消息集合
     */
    private static void HEART_BEAT_Assembly(StringBuilder sb, QyMsg qyMsg, ArrayList<byte[]> list) {
        sb.setCharAt(1,MsgTransfer. DATA_TYPE_2_CHAR(DataType.STRING));
        sb.append(MsgTransfer.BOOLEAN_2_SEGMENTATION(false));
        byte[] body = qyMsg.getFrom().getBytes(StandardCharsets.UTF_8);
        sb.append(MsgTransfer.getLength(body));
        byte[] header = sb.toString().getBytes(StandardCharsets.UTF_8);
        list.add(ArrayUtils.addAll(header, body));
    }

    /**
     * 常规消息组装
     *
     * @param qyMsg 消息体对象
     * @param sb    消息头SB
     * @param list  返回的消息集合
     */
    private static void NORM_MSG_Assembly(StringBuilder sb, QyMsg qyMsg, ArrayList<byte[]> list) throws IOException {

        if (DataType.JSON.equals(qyMsg.getDataType())) {

            byte[] body = JSON.toJSONString(qyMsg).getBytes(StandardCharsets.UTF_8);
            OUT_OF_LENGTH_MSG_Assembly(body, sb, list);
        } else if (DataType.OBJECT.equals(qyMsg.getDataType())) {

            byte[] body = IoUtil.objToSerializBytes(qyMsg);
            OUT_OF_LENGTH_MSG_Assembly(body, sb, list);
        } else if (DataType.STRING.equals(qyMsg.getDataType())) {

            byte[] body = (qyMsg.getFrom() + MsgHelper.gainMsg(qyMsg)).getBytes(StandardCharsets.UTF_8);
            OUT_OF_LENGTH_MSG_Assembly(body, sb, list);
        } else if (DataType.STREAM.equals(qyMsg.getDataType())) {

            byte[] bytes = qyMsg.getFrom().getBytes(StandardCharsets.UTF_8);
            OUT_OF_LENGTH_MSG_Assembly(ArrayUtils.addAll(bytes, (byte[]) MsgHelper.gainObjMsg(qyMsg)), sb, list);
        } else {
            byte[] bytes = qyMsg.getFrom().getBytes(StandardCharsets.UTF_8);
            OUT_OF_LENGTH_MSG_Assembly(ArrayUtils.addAll(bytes, (byte[]) MsgHelper.gainObjMsg(qyMsg)), sb, list);
        }
    }

    /**
     * 异常消息组装
     *
     * @param qyMsg 消息体对象
     * @param sb    消息头SB
     * @param list  返回的消息集合
     */
    private static void ERR_MSG_Assembly(StringBuilder sb, QyMsg qyMsg, ArrayList<byte[]> list) {
        if (DataType.JSON.equals(qyMsg.getDataType())) {

            byte[] body = JSON.toJSONString(qyMsg).getBytes(StandardCharsets.UTF_8);
            OUT_OF_LENGTH_MSG_Assembly(body, sb, list);
        } else {
            byte[] body = (qyMsg.getFrom() + MsgHelper.gainMsg(qyMsg)).getBytes(StandardCharsets.UTF_8);
            OUT_OF_LENGTH_MSG_Assembly(body, sb, list);
        }
    }

    /**
     * 对消息长度的判断  拆分并组装
     *
     * @param body 原消息体byte
     * @param sb   消息头SB
     * @param list 返回的消息集合
     */
    private static void OUT_OF_LENGTH_MSG_Assembly(byte[] body, StringBuilder sb, ArrayList<byte[]> list) {
        ArrayList<byte[]> bodyList = ArrayUtil.checkArrayLength(body, MsgTransfer.BODY_LENGTH_MAX);
        if (bodyList.size() == 1) {
            log.debug("单条消息 {}", new String(body, StandardCharsets.UTF_8));
            sb.append(MsgTransfer.BOOLEAN_2_SEGMENTATION(false));
            sb.append(MsgTransfer.getLength(body));
            byte[] header = sb.toString().getBytes(StandardCharsets.UTF_8);
            list.add(ArrayUtils.addAll(header, body));
        } else {
            log.debug("分片消息 {}", new String(body, StandardCharsets.UTF_8));
            sb.append(MsgTransfer.BOOLEAN_2_SEGMENTATION(true));
            String part_trade_id = RandomStringUtils.random(16, MsgTransfer.DICT);
            for (int i = 1; i <= bodyList.size(); i++) {
                StringBuilder builder = new StringBuilder(sb);
                byte[] cBody = bodyList.get(i - 1);
                builder.append(MsgTransfer.getLength(cBody));
                builder.append(part_trade_id);
                builder.append(Integer.toUnsignedString(i));
                builder.append(Integer.toUnsignedString(bodyList.size()));
                byte[] cHeader = builder.toString().getBytes(StandardCharsets.UTF_8);
                list.add(ArrayUtils.addAll(cHeader, cBody));
            }
        }
    }

}