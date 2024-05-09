package top.yqingyu.common.function;

import java.io.Serializable;

public interface Progress extends Serializable {
    void callback(String f, long total, long now);
}
