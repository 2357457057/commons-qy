package top.yqingyu.common.function;

import java.io.Serializable;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.function.ToBooleanBiFunction
 * @description
 * @createTime 2023年04月08日 01:58:00
 */
@FunctionalInterface
public interface ToBooleanBiFunction<T, U>  extends Serializable {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument.
     * @param u the second function argument.
     * @return the function result.
     */
    boolean applyAsBoolean(T t, U u);
}