package top.yqingyu.common.bean;

import com.alibaba.fastjson2.JSON;
import top.yqingyu.common.exception.QyRuntimeException;
import top.yqingyu.common.utils.CCConstants;
import top.yqingyu.common.utils.StringUtil;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static top.yqingyu.common.utils.ChinaCalendar.*;

public class HuangLi {
    //干支纪
    private String gz;
    private String nongLi;
    //年生肖
    private String shengxiao;
    //日宜
    private String yi;
    //日忌
    private String ji;
    //日值28星宿
    private String star28;
    //节气
    private String jieQi;
    //彭祖百忌
    private String pzbj;
    //日五行
    private String wuXingD;
    //年五行
    private String wuXingY;
    //胎神
    private String taiShen;
    //月相
    private String yueXiang;
    //吉神宜趋
    private String jsyq;
    //凶神集宜忌
    private String xsyj;
    //值神
    private String zhiShen;
    //建除十二神
    private String jc12Shen;
    //冲煞
    private String chongSha;

    private HuangLi() {
    }

    /**
     * @param time LocalDateTime/LocalDate
     * @return 黄历
     */
    public static HuangLi getInstance(Serializable time) {
        LocalDate date;
        if (time instanceof LocalDateTime localDateTime) {
            date = localDateTime.toLocalDate();
        } else if (time instanceof LocalDate localDate) {
            date = localDate;
        } else {
            throw new QyRuntimeException("unsupported time type {}", time == null ? "null" : time.getClass().getName());
        }
        LunarData lunarData = calcLunar(date);
        int[] gzYear = getGZYear(date);
        int[] gzDay = getGZDay(date);
        int[] gzMonth = getGZMonth(gzYear[0], lunarData.month);
        String gzyStr = getGzStr(gzYear);
        String gzmStr = getGzStr(gzMonth);
        String gzdStr = getGzStr(gzDay);
        String[] yiJi = YiJi(date, getGZDay(date));
        //因为要支持 LocalDateTime & LocalDate 所以对干支计时需特殊处理
        int[] gzHour = null;
        String gzhStr = null;
        if (time instanceof LocalDateTime now) {
            gzHour = getGZHour(now, gzDay[0]);
            gzhStr = getGzStr(gzHour);
        }

        String[] jiShenXiongShen = JiShenXiongShen(CCConstants.DZ[gzMonth[1]], gzdStr);
        String[] jq = JieQi(date);
        String jieQiStr;
        if (StringUtil.isEmpty(jq[0])) {
            jieQiStr = StringUtil.fillBrace("今天是{}的第{}天,距离{}还有{}天", jq[1], jq[2], jq[3], jq[4]);
        } else {
            jieQiStr = StringUtil.fillBrace("今天是{},距离{}还有{}天", jq[0], jq[3], jq[4]);
        }
        HuangLi huangLi = new HuangLi();
        huangLi.gz = StringUtil.fillBrace("{}年{}月{}日{}", gzyStr, gzmStr, gzdStr, StringUtil.isEmpty(gzhStr) ? "" : (gzhStr + "时"));
        huangLi.shengxiao = CCConstants.SHENGXIAO[gzYear[1]];
        huangLi.nongLi = StringUtil.fillBrace("农历{}{}年{}{}", gzyStr, huangLi.shengxiao, CCConstants.MONTH[lunarData.month - 1], CCConstants.DAY[lunarData.day - 1]);
        huangLi.yi = yiJi[0];
        huangLi.ji = yiJi[1];
        huangLi.star28 = STAR_28(date);
        huangLi.pzbj = PZBJ(gzDay);
        huangLi.jieQi = jieQiStr;
        huangLi.wuXingD = WuXing(gzDay);
        huangLi.wuXingY = WuXing(gzYear);
        huangLi.taiShen = TaiShen(CCConstants.DZ[gzMonth[1]], gzdStr);
        huangLi.yueXiang = YUE_XIANG(lunarData.day - 1);
        huangLi.jsyq = jiShenXiongShen[0];
        huangLi.xsyj = jiShenXiongShen[1];
        huangLi.zhiShen = ZhiShen(gzMonth[1], gzDay[1]);
        huangLi.jc12Shen = JianChu12Shen(date);
        huangLi.chongSha = ChongSha(gzDay[1]);
        return huangLi;
    }

    public String getGz() {
        return gz;
    }


    public String getShengxiao() {
        return shengxiao;
    }

    public String getYi() {
        return yi;
    }

    public String getJi() {
        return ji;
    }

    public String getStar28() {
        return star28;
    }

    public String getJieQi() {
        return jieQi;
    }

    public String getWuXingD() {
        return wuXingD;
    }

    public String getWuXingY() {
        return wuXingY;
    }

    public String getTaiShen() {
        return taiShen;
    }

    public String getYueXiang() {
        return yueXiang;
    }

    public String getNongLi() {
        return nongLi;
    }

    public String getPzbj() {
        return pzbj;
    }

    public String getJsyq() {
        return jsyq;
    }

    public String getXsyj() {
        return xsyj;
    }

    public String getZhiShen() {
        return zhiShen;
    }

    public String getJc12Shen() {
        return jc12Shen;
    }

    public String getChongSha() {
        return chongSha;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
