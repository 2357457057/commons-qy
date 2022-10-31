package top.yqingyu.common.nio$server.event$http.web.controller;

import top.yqingyu.common.nio$server.event$http.annotation.QyController;
import top.yqingyu.common.nio$server.event$http.compoment.HttpMethod;
import top.yqingyu.common.nio$server.event$http.compoment.LocationMapping;
import top.yqingyu.common.nio$server.event$http.exception.HttpException;
import top.yqingyu.common.utils.YamlUtil;
import top.yqingyu.common.utils.StringUtil;

import java.util.HashMap;

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
    public String showResource(String name, String path) {
        if (!"yyj".equals(name))
            throw new HttpException.MethodNotSupposedException("我C");
        StringBuilder sb = new StringBuilder();

        if (StringUtil.isBlank(path))
            path = "/";

        String[] split = path.split("/");

        int length = split.length == 0 ? 1 : split.length;

        String regx = path + ".*";

        HashMap<String, String> dir = new HashMap<>();
        HashMap<String, String> file = new HashMap<>();

        LocationMapping.FILE_RESOURCE_MAPPING.forEach((k, v) -> {
            if (k.indexOf("/") != 0) {
                k = "/" + k;
            }
            if (k.matches(regx)) {
                String[] ks = k.split("/");
                if (ks.length == length) {
                    file.put(k, ks[length - 1]);
                } else if (ks.length > length) {
                    dir.put(k, ks[length - 1]);
                }
            }
        });

        if (!"/".equals(path)) {

            StringBuilder pathBuilder = new StringBuilder();
            for (String s : split) {
                pathBuilder.append("/").append(s);
            }
            path = pathBuilder.toString();
            sb.append("<a href = '").append("/root/file?name=yyj&path=").append(path).append("'>")
                    .append("..")
                    .append("</a>")
                    .append("<br>");
        }

        dir.forEach((k,v)->{
            sb.append("<a href = '").append("/root/file?name=yyj&path=").append(k).append("'>")
                    .append(v)
                    .append("</a>")
                    .append("<br>");
        });

        file.forEach((k,v)->{
            sb.append("<a href = '").append(k).append("'>")
                    .append(v)
                    .append("</a>")
                    .append("<br>");
        });


        return sb.toString();
    }
}
