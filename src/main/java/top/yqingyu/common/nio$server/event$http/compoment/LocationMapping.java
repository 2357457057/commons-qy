package top.yqingyu.common.nio$server.event$http.compoment;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.asm.impl.MethodParamGetter;
import top.yqingyu.common.nio$server.event$http.annotation.QyController;
import top.yqingyu.common.nio$server.event$http.entity.*;
import top.yqingyu.common.nio$server.event$http.exception.HttpException;
import top.yqingyu.common.qydata.DataMap;
import top.yqingyu.common.utils.ClazzUtil;
import top.yqingyu.common.utils.StringUtil;
import top.yqingyu.common.utils.YamlUtil;

import java.io.File;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$http.entity.LocationMapping
 * @description
 * @createTime 2022年09月10日 22:56:00
 */
public class LocationMapping {

    private static final Logger log = LoggerFactory.getLogger(LocationMapping.class);

    public static final ConcurrentHashMap<String, String> FILE_RESOURCE_MAPPING = new ConcurrentHashMap<>();

    public static final ConcurrentHashMap<String, Bean> BEAN_RESOURCE_MAPPING = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, String> FILE_CACHING = new ConcurrentHashMap<>();

    public static void loadingFileResource(String rootPath) {
        HashMap<String, String> mapping = YamlUtil.getFilePathMapping(rootPath);
        FILE_RESOURCE_MAPPING.putAll(mapping);
    }

    public static void loadingBeanResource() {

        List<Class<?>> classes = ClazzUtil.getClassListByAnnotation("top.yqingyu.common", QyController.class);

        for (Class<?> aClass : classes) {
            QyController annotation = aClass.getAnnotation(QyController.class);
            String parentPath = annotation.path();
            Object instance = null;
            Method[] methods = aClass.getDeclaredMethods();

            Constructor<?>[] aClassConstructors = aClass.getConstructors();

            for (Constructor<?> constructor : aClassConstructors) {
                Parameter[] parameters = constructor.getParameters();
                if (parameters.length == 0) {
                    try {
                        instance = constructor.newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        log.error("bean mapping error constructor error ", e);
                    }
                }
            }

            for (Method method : methods) {
                if (method.trySetAccessible()) {
                    method.setAccessible(true);
                }

                QyController methodAnnotation = method.getAnnotation(QyController.class);

                if (methodAnnotation != null) {
                    Bean bean = new Bean();
                    String methodPath = methodAnnotation.path();
                    HttpMethod[] httpMethods = methodAnnotation.method();
                    bean.setType(aClass);
                    bean.setObj(instance);
                    bean.setHttpMethods(httpMethods);
                    bean.setMethod(method);
                    String[] parameterNames = MethodParamGetter.doGetParameterNames(method);
                    bean.setMethodParamName(parameterNames);
                    if (methodPath.indexOf("/") != 0) {
                        methodPath = "/" + methodPath;
                    }
                    String path = parentPath + methodPath;

                    if (path.indexOf("/") != 0) {
                        path = "/" + path;
                    }
                    BEAN_RESOURCE_MAPPING.put(path, bean);
                    log.debug("add bean mapping {}  > {}", path, bean);
                }
            }
        }
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

        if (StringUtils.isBlank(s)) {
            if (YamlUtil.isWindows())
                s = FILE_RESOURCE_MAPPING.get(url.replaceFirst("\\\\", ""));
            else
                s = FILE_RESOURCE_MAPPING.get(url.replaceFirst("/", ""));

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
                    .setStatue_code(stateCode);
            response.setFile_body(file);
            response.setAssemble(true);
        }
    }

    public static void beanResourceMapping(Request request, Response response) {
        String url = request.getUrl();
        String[] urls = url.split("[?]");
        url = urls[0];

        Bean bean = BEAN_RESOURCE_MAPPING.get(url);
        try {

            if (bean != null) {
                ContentType requestCtTyp = ContentType.parse(request.getHeader("Content-Type"));

                //校验请求方法
                matchHttpMethod(bean, request);

                Object invokeObj = bean.getObj();
                Method invokeMethod = bean.getMethod();

                Parameter[] parameters = invokeMethod.getParameters();
                Object[] args = new Object[parameters.length];

                if (HttpMethod.GET.equals(request.getMethod())) {
                    //================================================================
                    //==       优先拼装 request/response 其次属性名称 其次数量           ==
                    //================================================================
                    if (parameters.length == 1) {
                        Type paramType = parameters[0].getParameterizedType();
                        String typeName = paramType.getTypeName();
                        if (Request.class.getName().equals(typeName)) {
                            args[0] = request;
                        } else if (Response.class.getName().equals(typeName)) {
                            args[0] = request;
                        } else {
                            DataMap urlParam = request.getUrlParam();
                            if (urlParam.size() == 1) {
                                urlParam.forEach((k, v) -> args[0] = v);
                            } else if (urlParam.size() > 1) {
                                String name = parameters[0].getName();
                                urlParam.forEach((k, v) -> {
                                    if (name.equals(k)) {
                                        args[0] = v;
                                    } else if (name.equalsIgnoreCase(k)) {
                                        args[0] = v;
                                    }
                                });

                            }
                        }
                    } else if (parameters.length > 1) {

                    }
                } else if (HttpMethod.POST.equals(request.getMethod())) {
                    if (parameters.length == 1) {

                        Type paramType = parameters[0].getParameterizedType();
                        String typeName = paramType.getTypeName();

                        if (Request.class.getName().equals(typeName)) {
                            args[0] = request;
                        } else if (Response.class.getName().equals(typeName)) {
                            args[0] = request;

                        } else if (ClazzUtil.isBaseType(paramType)) {
                            args[0] = new String(request.gainBody(), requestCtTyp.getCharset() == null ? StandardCharsets.UTF_8 : requestCtTyp.getCharset());
                        } else {
                            args[0] = JSON.parseObject(request.gainBody(), paramType);
                        }
                    } else if (parameters.length > 1) {

                    }
                }

//======================================================================================================
                Object methodReturn = invokeMethod.invoke(invokeObj, args);
//======================================================================================================

                if (methodReturn instanceof String) {
                    response.setString_body((String) methodReturn)
                            .putHeaderContentType(ContentType.TEXT_HTML);
                } else {
                    response
                            .setString_body(JSON.toJSONString(methodReturn))
                            .putHeaderContentType(ContentType.APPLICATION_JSON);
                }
                response
                        .setStatue_code("200")
                        .putHeaderAcceptRanges()
                        .setAssemble(true);
            }

        } catch (HttpException.MethodNotSupposedException e) {
            response
                    .setString_body("请求方法不支持")
                    .setStatue_code("400")
                    .putHeaderContentType(ContentType.TEXT_PLAIN)
                    .putHeaderDate(ZonedDateTime.now())
                    .setAssemble(true);

        } catch (Exception e) {
            response
                    .setString_body("呜呜，人家坏掉了")
                    .setStatue_code("500")
                    .putHeaderContentType(ContentType.TEXT_PLAIN)
                    .putHeaderDate(ZonedDateTime.now())
                    .setAssemble(true);
            log.error("", e);
        }

    }


    static void matchHttpMethod(Bean bean, Request request) throws HttpException.MethodNotSupposedException {
        HttpMethod[] httpMethods = bean.getHttpMethods();
        HttpMethod method = request.getMethod();
        boolean $throws = true;
        for (HttpMethod httpMethod : httpMethods) {
            if (method.equals(httpMethod)) {
                $throws = false;
                break;
            }
        }
        if ($throws) throw new HttpException.MethodNotSupposedException("请求方法不支持！");
    }

}
