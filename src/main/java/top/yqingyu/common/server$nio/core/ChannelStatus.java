package top.yqingyu.common.server$nio.core;

import top.yqingyu.common.qydata.ConcurrentQyMap;

/**
 * @author Yangy
 * @description 读写分离 为channel加读写状态
 */
public interface ChannelStatus {
    String WRITE = "writing";
    String READ = "reading";
}
