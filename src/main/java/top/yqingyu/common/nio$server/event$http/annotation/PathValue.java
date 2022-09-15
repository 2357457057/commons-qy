package top.yqingyu.common.nio$server.event$http.annotation;

import java.lang.annotation.*;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$http.annotation.PathValue
 * @description
 * @createTime 2022年09月15日 22:51:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target(ElementType.PARAMETER)
public @interface PathValue {
    String name();
}
