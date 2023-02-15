package top.yqingyu.common.annotation;

import java.lang.annotation.*;

/**
 * 用于初始化资源 配合
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Init {
}
