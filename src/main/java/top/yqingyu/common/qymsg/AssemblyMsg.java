package top.yqingyu.common.qymsg;

import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.utils.ArrayUtil;
import top.yqingyu.common.utils.IoUtil;
import top.yqingyu.common.utils.StringUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static top.yqingyu.common.qymsg.Dict.*;
import static top.yqingyu.common.qymsg.MsgTransfer.MSG_LENGTH_RADIX;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.qymsg.AssemblyMsg
 * @description
 * @createTime 2022年09月06日 10:36:00
 */

class AssemblyMsg {
    private static final Logger log = LoggerFactory.getLogger(AssemblyMsg.class);

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

        switch (qyMsg.getMsgType()) {
            case AC -> AC_Assembly(sb, qyMsg, list);
            case HEART_BEAT -> HEART_BEAT_Assembly(sb, qyMsg, list);
            case ERR_MSG -> ERR_MSG_Assembly(sb, qyMsg, list);
            default -> NORM_MSG_Assembly(sb, qyMsg, list);
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
        list.add(ArrayUtil.addAll(header, body));
    }

    /**
     * 心跳消息组装
     *
     * @param qyMsg 消息体对象
     * @param sb    消息头SB
     * @param list  返回的消息集合
     */
    private static void HEART_BEAT_Assembly(StringBuilder sb, QyMsg qyMsg, ArrayList<byte[]> list) {
        sb.setCharAt(1, MsgTransfer.DATA_TYPE_2_CHAR(DataType.STRING));
        sb.append(MsgTransfer.BOOLEAN_2_SEGMENTATION(false));
        byte[] body = qyMsg.getFrom().getBytes(StandardCharsets.UTF_8);
        sb.append(MsgTransfer.getLength(body));
        byte[] header = sb.toString().getBytes(StandardCharsets.UTF_8);
        list.add(ArrayUtil.addAll(header, body));
    }

    /**
     * 常规消息组装
     *
     * @param qyMsg 消息体对象
     * @param sb    消息头SB
     * @param list  返回的消息集合
     */
    private static void NORM_MSG_Assembly(StringBuilder sb, QyMsg qyMsg, ArrayList<byte[]> list) throws IOException {

        byte[] body;
        switch (qyMsg.getDataType()) {
            case OBJECT -> body = IoUtil.objToSerializBytes(qyMsg);

            case STRING -> body = (qyMsg.getFrom() + MsgHelper.gainMsg(qyMsg)).getBytes(StandardCharsets.UTF_8);

            case STREAM ->
                    body = ArrayUtil.addAll(qyMsg.getFrom().getBytes(StandardCharsets.UTF_8), (byte[]) MsgHelper.gainObjMsg(qyMsg));
            // JSON FILE
            default -> body = JSON.toJSONString(qyMsg).getBytes(StandardCharsets.UTF_8);

        }
        OUT_OF_LENGTH_MSG_Assembly(body, sb, list);
    }

    /**
     * 异常消息组装
     *
     * @param qyMsg 消息体对象
     * @param sb    消息头SB
     * @param list  返回的消息集合
     */
    private static void ERR_MSG_Assembly(StringBuilder sb, QyMsg qyMsg, ArrayList<byte[]> list) {
        byte[] body;
        if (DataType.JSON.equals(qyMsg.getDataType())) {
            body = JSON.toJSONString(qyMsg).getBytes(StandardCharsets.UTF_8);
        } else {
            body = (qyMsg.getFrom() + MsgHelper.gainMsg(qyMsg)).getBytes(StandardCharsets.UTF_8);
        }
        OUT_OF_LENGTH_MSG_Assembly(body, sb, list);
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
            log.debug("single msg {}", new String(body, StandardCharsets.UTF_8));
            sb.append(MsgTransfer.BOOLEAN_2_SEGMENTATION(false));
            sb.append(MsgTransfer.getLength(body));
            byte[] header = sb.toString().getBytes(StandardCharsets.UTF_8);
            list.add(ArrayUtil.addAll(header, body));
        } else {
            sb.append(MsgTransfer.BOOLEAN_2_SEGMENTATION(true));
            String part_trade_id = RandomStringUtils.random(PARTITION_ID_LENGTH, MsgTransfer.DICT);
            for (int i = 1; i <= bodyList.size(); i++) {
                StringBuilder builder = new StringBuilder(sb);
                byte[] cBody = bodyList.get(i - 1);
                builder.append(MsgTransfer.getLength(cBody));
                builder.append(part_trade_id);
                builder.append(StringUtil.leftPad(Integer.toUnsignedString(i, MSG_LENGTH_RADIX), NUMERATOR_LENGTH, '0'));
                builder.append(StringUtil.leftPad(Integer.toUnsignedString(bodyList.size(), MSG_LENGTH_RADIX), DENOMINATOR_LENGTH, '0'));
                byte[] cHeader = builder.toString().getBytes(StandardCharsets.UTF_8);
                byte[] bytes = ArrayUtil.addAll(cHeader, cBody);
                list.add(bytes);
                log.debug("part msg {}", new String(bytes, StandardCharsets.UTF_8));
            }
        }
    }

}
