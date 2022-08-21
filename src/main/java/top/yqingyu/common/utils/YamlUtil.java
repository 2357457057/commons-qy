package top.yqingyu.common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.yaml.snakeyaml.Yaml;
import top.yqingyu.common.qydata.DataMap;
import top.yqingyu.common.qydata.DatasetList;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
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

    public DatasetList getDataList(String key) {
        return cfgData.getDataset(key);
    }

    public DataMap getCfgData() {
        return cfgData;
    }

    private void setCfgData(DataMap cfgData) {
        this.cfgData = cfgData;
    }

    /**
     * @param fileName 配置文件名称
     * @return Util
     * @author YYJ
     * @version 1.0.0
     * @description 加载配置文件
     */
    public static YamlUtil loadYaml(String fileName) {

        YamlUtil yamlUtil = new YamlUtil();

        Yaml yaml = new Yaml();
        DataMap cfgData = new DataMap();
        yamlUtil.setCfgData(cfgData);

        HashMap<String, File> map = getYaml(fileName);

        map.forEach((k, v) -> {
            try {
                HashMap hashMap = yaml.loadAs(new FileInputStream(v), HashMap.class);

                String s = JSON.toJSONString(hashMap);
                cfgData.put(k, JSON.parseObject(s));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        return yamlUtil;
    }

    public static HashMap<String, File> getYaml(String cfgFileName) {
        return getConfigFile(cfgFileName, ".*[.](yml|yaml)");
    }



    /**
     * @param cfgFileName 配置文件名称
     * @param target_Regx 目标文件正则表达式
     * @return 目标文件list
     * @author YYJ
     * @description 获取运行路径中的文件
     */
    public static HashMap<String, File> getConfigFile(String cfgFileName, String target_Regx) {

        File file = new File(System.getProperty("user.dir"));

        AtomicInteger atomicInteger = new AtomicInteger();

        Pattern tgPattern = Pattern.compile(target_Regx);


        File[] files = file.listFiles((dir, name) -> {
            if (dir.isDirectory())
                return true;
            Matcher matcher = tgPattern.matcher(name);
            return matcher.find();
        });

        HashMap<String, File> map = new HashMap<>();
        LinkedList<File> queue = new LinkedList<>(Arrays.asList(files));
        do {
            File poll = queue.poll();
            if (poll != null) {

                File[] listFiles = null;
                if (poll.isDirectory()) {
                    listFiles = poll.listFiles((dir, name) -> {
                        if (dir.isDirectory())
                            return true;
                        Matcher matcher = tgPattern.matcher(name);
                        return matcher.find();
                    });
                }
                if (listFiles != null && listFiles.length > 0)
                    queue.addAll(Arrays.asList(listFiles));

                if (poll.isFile()) {
                    String name = poll.getName();
                    Matcher matcher = tgPattern.matcher(name);
                    if (name.contains(cfgFileName) && matcher.find()) {
                        if (map.get(name) != null)
                            map.put(name + "_" + atomicInteger.getAndIncrement(), poll);
                        else
                            map.put(name, poll);
                    }
                }
            }
        } while (queue.size() > 0);

        return map;
    }
}
