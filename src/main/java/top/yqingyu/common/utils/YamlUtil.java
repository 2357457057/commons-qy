package top.yqingyu.common.utils;

import com.alibaba.fastjson2.JSON;
import org.yaml.snakeyaml.Yaml;
import top.yqingyu.common.qydata.DataList;
import top.yqingyu.common.qydata.DataMap;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static top.yqingyu.common.utils.ResourceUtil.getConfigFileInner;
import static top.yqingyu.common.utils.ResourceUtil.getConfigFileOuter;

/**
 * Yaml 的资源加载。
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.utils.YamlUtil
 * @description
 * @createTime 2022年08月21日 14:42:00
 */
public class YamlUtil {
    private DataMap cfgData;


    private YamlUtil() {
    }

    public String getString(String key) {
        return cfgData.getString(key);
    }

    public Object getObject(String key) {
        return cfgData.get(key);
    }

    public DataMap getDataMap(String key) {
        return cfgData.getData(key);
    }

    public DataList getDataList(String key) {
        return cfgData.getDataList(key);
    }

    public DataMap getCfgData() {
        return cfgData;
    }

    private void setCfgData(DataMap cfgData) {
        this.cfgData = cfgData;
    }

    /**
     * @param fileName 配置文件名称
     * @param loadType 加载状态 外部文件 IN、内部文件 OUT、 均加载 BOTH
     * @return Util
     * @author YYJ
     * @description 加载配置文件
     */
    public static YamlUtil loadYaml(String fileName, LoadType loadType) {

        YamlUtil yamlUtil = new YamlUtil();
        Yaml yaml = new Yaml();
        DataMap cfgData = new DataMap();
        yamlUtil.setCfgData(cfgData);

        AtomicInteger atomicInteger = new AtomicInteger();
        if (loadType == LoadType.OUTER || loadType == LoadType.BOTH) {
            HashMap<String, File> map = getYamlOuter(fileName);
            map.forEach((k, v) -> {
                try {
                    FileInputStream inputStream = new FileInputStream(v);
                    HashMap hashMap = yaml.loadAs(inputStream, HashMap.class);

                    String s = JSON.toJSONString(hashMap);
                    if (cfgData.containsKey(k))
                        cfgData.put(k + "_" + atomicInteger.getAndIncrement(), JSON.parseObject(s));
                    else
                        cfgData.put(k, JSON.parseObject(s));
                    inputStream.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        if (loadType == LoadType.OUTER || loadType == LoadType.BOTH) {
            try {
                HashMap<String, InputStream> mapIn = getYamlInner(fileName);

                mapIn.forEach((k, v) -> {
                    HashMap hashMap = yaml.loadAs(v, HashMap.class);
                    try {
                        v.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String s = JSON.toJSONString(hashMap);
                    if (cfgData.containsKey(k))
                        cfgData.put(k + "_" + atomicInteger.getAndIncrement(), JSON.parseObject(s));
                    else
                        cfgData.put(k, JSON.parseObject(s));

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return yamlUtil;
    }

    /**
     * @param fileName 配置文件名称
     * @param loadType 加载状态 外部文件 IN、内部文件 OUT、 均加载 BOTH
     * @return Util
     * @author YYJ
     * @description 加载配置文件
     */
    public static YamlUtil loadYaml(String path, String fileName, LoadType loadType) {

        YamlUtil yamlUtil = new YamlUtil();
        Yaml yaml = new Yaml();
        DataMap cfgData = new DataMap();
        yamlUtil.setCfgData(cfgData);

        if (loadType == LoadType.OUTER || loadType == LoadType.BOTH) {
            HashMap<String, File> map = getYamlOuter(path, fileName);
            map.forEach((k, v) -> {
                try {
                    HashMap hashMap = yaml.loadAs(new FileInputStream(v), HashMap.class);

                    String s = JSON.toJSONString(hashMap);
                    cfgData.put(k, JSON.parseObject(s));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return yamlUtil;
    }

    public static HashMap<String, File> getYamlOuter(String cfgFileName) {
        return getYamlOuter(System.getProperty("user.dir"), cfgFileName);
    }

    public static HashMap<String, File> getYamlOuter(String path, String cfgFileName) {
        return getConfigFileOuter(path, cfgFileName, ".*[.](yml|yaml)");
    }

    public static HashMap<String, InputStream> getYamlInner(String cfgFileName) throws IOException {
        DataList configFileInner = getConfigFileInner(cfgFileName, ".*[.](yml|yaml)");

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        HashMap<String, InputStream> map = new HashMap<>();
        AtomicInteger atomicInteger = new AtomicInteger();
        configFileInner.forEach(a -> {
            DataMap data = (DataMap) a;
            if (map.containsKey("name"))
                map.put(data.getString("name") + "_" + atomicInteger.getAndIncrement(), classLoader.getResourceAsStream(data.getString("path")));
            else
                map.put(data.getString("name"), classLoader.getResourceAsStream(data.getString("path")));
        });

        return map;
    }

    public enum LoadType {
        OUTER, INNER, BOTH;
    }

}
