package top.yqingyu.common.function;

public interface Progress {
    void callback(String f, long total, long now);
}
