package top.yqingyu.common.utils;

import top.yqingyu.common.qydata.ConcurrentQyMap;

public interface Status {
    static void statusTrue(ConcurrentQyMap<String, Object> map, String statusKey) {
        map.put(statusKey, Boolean.TRUE);
    }

    static void statusFalse(ConcurrentQyMap<String, Object> map, String statusKey) {
        map.put(statusKey, Boolean.FALSE);
    }

    static boolean canDo(ConcurrentQyMap<String, Object> map, String statusKey) {
        // map.get(statusKey, Boolean.class 可能为 null
        return map.get(statusKey, Boolean.class) == Boolean.FALSE || map.get(statusKey, Boolean.class) == null;
    }
}
