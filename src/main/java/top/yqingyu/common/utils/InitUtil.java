package top.yqingyu.common.utils;

import top.yqingyu.common.annotation.Init;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 可用于初始化一些类的数据 配合注解 {@link Init}使用
 */
public class InitUtil {
    public static void init(String basePackage) {
        List<Method> methods = ClazzUtil.getMethodByAnno(basePackage, Init.class, true);
        for (Method method : methods) {
            try {
                Constructor<?>[] constructors = method.getDeclaringClass().getConstructors();
                if (constructors.length < 1) continue;
                int parameterCount = constructors[0].getParameterCount();
                method.setAccessible(true);
                ReflectionUtil.invokeMethod(method, constructors[0].newInstance(new Object[parameterCount]));
            } catch (Exception e) {
                ReflectionUtil.handleReflectionException(e);
            }

        }
    }
}
