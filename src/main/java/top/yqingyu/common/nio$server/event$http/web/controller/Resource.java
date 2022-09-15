package top.yqingyu.common.nio$server.event$http.web.controller;

import top.yqingyu.common.nio$server.event$http.annotation.QyController;
import top.yqingyu.common.nio$server.event$http.compoment.LocationMapping;
import top.yqingyu.common.nio$server.event$http.entity.HttpMethod;
import top.yqingyu.common.qydata.DataMap;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$http.web.controller.Resource
 * @description
 * @createTime 2022年09月15日 16:48:00
 */
@QyController(path = "root")
public class Resource {

    @QyController(path = "file", method = {HttpMethod.POST, HttpMethod.GET})
    public String showResource(DataMap dataMap) {
        StringBuilder sb = new StringBuilder();
        LocationMapping.FILE_RESOURCE_MAPPING.forEach((k, v) -> sb.append(k).append("<br>"));

        return sb.toString();
    }
}
