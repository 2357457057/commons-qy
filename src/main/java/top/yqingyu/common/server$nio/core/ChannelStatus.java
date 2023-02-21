package top.yqingyu.common.server$nio.core;

import top.yqingyu.common.qydata.ConcurrentQyMap;

/**
 * @author Yangy
 * @description 读写分离 为channel加读写状态
 */
public interface ChannelStatus {
    String WRITE = "writing";
    String READ = "reading";

    static void statusTrue(ConcurrentQyMap<String, Object> map, String statusKey) {
        map.put(statusKey, Boolean.TRUE);
    }

    static void statusFalse(ConcurrentQyMap<String, Object> map, String statusKey) {
        map.put(statusKey, Boolean.TRUE);
    }

    static boolean canDo(ConcurrentQyMap<String, Object> map, String statusKey) {
        // map.get("reading", Boolean.class 可能为 null
        return map.get(statusKey, Boolean.class) == Boolean.FALSE || map.get(statusKey, Boolean.class) == null;
    }
}
