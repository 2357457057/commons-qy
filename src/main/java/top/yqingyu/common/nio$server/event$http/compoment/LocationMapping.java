package top.yqingyu.common.nio$server.event$http.compoment;

import cn.hutool.core.lang.UUID;
import org.apache.commons.lang3.StringUtils;
import top.yqingyu.common.nio$server.event$http.entity.Bean;
import top.yqingyu.common.nio$server.event$http.entity.ContentType;
import top.yqingyu.common.nio$server.event$http.entity.Request;
import top.yqingyu.common.nio$server.event$http.entity.Response;
import top.yqingyu.common.utils.StringUtil;
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

    public static final ConcurrentHashMap<String, Bean> BEAN_RESOURCE_MAPPING = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, String> FILE_CACHING = new ConcurrentHashMap<>();

    public static void loadingFileResource(String rootPath) {
        HashMap<String, String> mapping = YamlUtil.getFilePathMapping(rootPath);
        FILE_RESOURCE_MAPPING.putAll(mapping);
    }

    public static void fileResourceMapping(Request request, Response response) {
        String url = request.getUrl();
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

            //当且仅当完全相同时可采用缓存
            if (StringUtil.equalsNull(eTag, eTagValue)) {
                stateCode = "304";
                response.putHeader("ETag", eTag);
            } else if (StringUtils.isNotBlank(eTagValue)) {
                response.putHeader("ETag", eTagValue);
            } else {
                eTag = "W/\"" + UUID.randomUUID() + "\"";
                response.putHeader("ETag", eTag);
                FILE_CACHING.put(url, eTag);
            }

            response
                    .putHeaderContentType(contentType)
                    .putHeaderAcceptRanges()
                    .putHeaderContentLength(file.length())
                    .setStatue_code(stateCode);
            response.setFile_body(file);
            response.setAssemble(true);
        }
    }

    public static void beanResourceMapping(Request request, Response response) {
        String url = request.getUrl();
        String[] urls = url.split("[?]");

    }
}
