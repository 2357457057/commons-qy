package top.yqingyu.common.utils;

import org.yaml.snakeyaml.Yaml;
import top.yqingyu.common.qydata.DataMap;
import top.yqingyu.common.qydata.DatasetList;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
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
     *
     * @param fileName 配置文件名称
     * @return Util
     * @author YYJ
     * @version 1.0.0
     * @description      */
    public static YamlUtil loadYaml(String fileName){

        YamlUtil yamlUtil = new YamlUtil();

        Yaml yaml = new Yaml();
        DataMap cfgData = new DataMap();
        yamlUtil.setCfgData(cfgData);

        HashMap<String, File> map = getYaml(fileName);

        map.forEach((k,v)->{
            try {
                DataMap dataMap = yaml.loadAs(new FileInputStream(v), DataMap.class);
                cfgData.put(k,dataMap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

       return yamlUtil;
    }

    public static HashMap<String, File> getYaml(String cfgFileName) {
        return getConfigFile("application", ".*[.](yml|yaml)");
    }

    /**
     *
     * @param cfgFileName 配置文件名称
     * @param target_Regx 目标文件正则表达式
     * @return  目标文件list
     * @author YYJ
     * @description 获取运行路径中的文件
     *
     * */
    public static HashMap<String, File> getConfigFile(String cfgFileName, String target_Regx) {

        File file = new File(System.getProperty("user.dir"));


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
                    if (name.contains(cfgFileName) && matcher.find())
                        map.put(name, poll);
                }
            }
        } while (queue.size() > 0);

        return map;
    }
}
