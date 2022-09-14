package top.yqingyu.common.nio$server.event$http.annotation;

import top.yqingyu.common.nio$server.event$http.entity.HttpMethod;

import java.lang.annotation.*;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$http.annotation.RestController
 * @description
 * @createTime 2022年09月14日 11:26:00
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RestController {

    HttpMethod[] method() default {HttpMethod.POST};

    String path() default "";

}
