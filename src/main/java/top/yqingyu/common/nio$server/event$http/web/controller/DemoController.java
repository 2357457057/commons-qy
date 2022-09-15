package top.yqingyu.common.nio$server.event$http.web.controller;

import top.yqingyu.common.nio$server.event$http.annotation.QyController;
import top.yqingyu.common.nio$server.event$http.entity.HttpMethod;
import top.yqingyu.common.qydata.DataMap;
import top.yqingyu.common.utils.LocalDateTimeUtil;

import java.time.ZonedDateTime;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$http.DemoController
 * @description
 * @createTime 2022年09月15日 14:09:00
 */
@QyController(path = "qy_demo")
public class DemoController {

    @QyController(path = "demo1", method = {HttpMethod.POST, HttpMethod.GET})
    public String demo1(DataMap dataMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello Qy Framework  ~").append("<br>");
        sb.append("demo1").append("<br>");
        sb.append(LocalDateTimeUtil.HTTP_FORMATTER.format(ZonedDateTime.now()));

        return sb.toString();
    }

    @QyController(path = "demo2", method = {HttpMethod.POST})
    public String demo2(String yyj, DataMap data, String aa) {
        System.out.println(yyj);
        System.out.println(data);
        System.out.println(aa);
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>").append("Hello~ Qy Framework  ~").append("</h1>");
        sb.append("<h2>").append("demo2").append("</h2>");
        sb.append(LocalDateTimeUtil.HTTP_FORMATTER.format(ZonedDateTime.now()));
        return sb.toString();
    }
}
