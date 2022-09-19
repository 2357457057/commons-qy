package top.yqingyu.common.nio$server.event$http.web.controller;

import top.yqingyu.common.nio$server.event$http.annotation.QyController;
import top.yqingyu.common.nio$server.event$http.compoment.HttpMethod;
import top.yqingyu.common.nio$server.event$http.compoment.LocationMapping;
import top.yqingyu.common.nio$server.event$http.exception.HttpException;
import top.yqingyu.common.utils.YamlUtil;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$http.web.controller.Resource
 * @description
 * @createTime 2022年09月15日 16:48:00
 */
@QyController(path = "root")
public class Resource {

    @QyController(path = "file", method = {HttpMethod.GET})
    public String showResource(String name) {
        if (!"yyj".equals(name))
            throw new HttpException.MethodNotSupposedException("我C");

        StringBuilder sb = new StringBuilder();
        LocationMapping.FILE_RESOURCE_MAPPING.forEach((k, v) -> {
            sb.append("<a href = '");
            if (YamlUtil.isWindows() && k.indexOf("/") != 0) {
                sb.append("/");
            }
            sb.append(k.replaceAll("\\\\", "/")).append("'>").append(k).append("</a>").append("<br>");
        });
        return sb.toString();
    }
}
