package top.yqingyu.common.nio$server.event$http.compoment;

import cn.hutool.core.lang.UUID;
import org.apache.commons.lang3.StringUtils;
import top.yqingyu.common.nio$server.event$http.entity.ContentType;
import top.yqingyu.common.nio$server.event$http.entity.Request;
import top.yqingyu.common.nio$server.event$http.entity.Response;
import top.yqingyu.common.utils.YamlUtil;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$http.entity.LocationMapping
 * @description
 * @createTime 2022年09月10日 22:56:00
 */
public class LocationMapping {

    public static final ConcurrentHashMap<String, String> FILE_RESOURCE_MAPPING = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, String> FILE_CACHING = new ConcurrentHashMap<>();

    public static void loadingFileResource(String rootPath) {
        HashMap<String, String> mapping = YamlUtil.getFilePathMapping(rootPath);
        FILE_RESOURCE_MAPPING.putAll(mapping);
    }

    public static File initResourceHeader(String url, Response response, Request request) {

        //去掉问号。 TODO 将get请求参数拼接到Request
        String[] urls = url.split("[?]");
        url = urls[0];

        if (YamlUtil.isWindows()) url = url.replaceAll("/", "\\\\");
        String s = FILE_RESOURCE_MAPPING.get(url);

        if (StringUtils.isBlank(s)) {
            s = FILE_RESOURCE_MAPPING.get(url + ".html");
        }

        if (YamlUtil.isWindows()) {
            url = url + "\\";
            url = url.replace("\\\\", "\\");
        } else {
            url = url + "/";
            url = url.replace("//", "/");
        }

        if (StringUtils.isBlank(s)) {
            s = FILE_RESOURCE_MAPPING.get(url + "index.html");
        }
        if (StringUtils.isBlank(s)) {
            s = FILE_RESOURCE_MAPPING.get(url + "index.htm");
        }

        if (StringUtils.isNotBlank(s)) {
            File file = new File(s);
            ContentType contentType = ContentType.parseContentType(s);
            String stateCode = "200";

            String eTag = request.getHeader("If-None-Match");
            String eTagValue = FILE_CACHING.get(url);

            //部分资源缓存
            if (StringUtils.isNotBlank(eTag) && StringUtils.isNotBlank(eTagValue) && StringUtils.equals(eTag, eTagValue)) {
                stateCode = "304";
                response.putHeader("ETag", eTag);
            } else {
                eTag = "W/\"" + UUID.randomUUID() + "\"";
                response.putHeader("ETag", eTag);
                FILE_CACHING.put(url, eTag);
            }

            response
                    .putHeaderContentType(contentType)
                    .putHeaderAcceptRanges()
                    .putHeaderContentLength(file.length())
                    .setState_code(stateCode);
            return file;
        }
        response.putHeaderContentType(ContentType.DEFAULT_TEXT);
        response.setState_code("404");
        response.setBody("木有资源啦 ^ Ω ^");
        return null;
    }
}
