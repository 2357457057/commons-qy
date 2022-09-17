package top.yqingyu.common.utils;

import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import top.yqingyu.common.qydata.DataMap;
import top.yqingyu.common.qydata.DataList;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
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
                    HashMap hashMap = yaml.loadAs(new FileInputStream(v), HashMap.class);

                    String s = JSON.toJSONString(hashMap);
                    if (cfgData.containsKey(k))
                        cfgData.put(k + "_" + atomicInteger.getAndIncrement(), JSON.parseObject(s));
                    else
                        cfgData.put(k, JSON.parseObject(s));
                } catch (FileNotFoundException e) {
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


    /**
     * @param cfgFileName 配置文件名称
     * @param target_Regx 目标文件正则表达式
     * @return 目标文件list
     * @author YYJ
     * @description 运行路径中的文件
     */
    public static HashMap<String, File> getConfigFileOuter(String rootPath, String cfgFileName, String target_Regx) {

        File file = new File(rootPath);

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


    public static DataList getConfigFileInner(String cfgFileName, String target_Regx) throws IOException {


        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources("/");
        Iterator<URL> urlIterator = resources.asIterator();

        DataList list = new DataList();

        while (urlIterator.hasNext()) {
            URL url = urlIterator.next();
            String protocol = url.getProtocol();
            if (protocol.equals("jar")) {
                JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                JarFile jarFile = jarURLConnection.getJarFile();
                Enumeration<JarEntry> jarEntries = jarFile.entries();
                while (jarEntries.hasMoreElements()) {
                    JarEntry jarEntry = jarEntries.nextElement();
                    String jarEntryName = jarEntry.getName();
                    if (jarEntryName.contains(cfgFileName) && jarEntryName.matches(target_Regx)) {
                        DataMap dataMap = new DataMap();
                        dataMap.put("path", jarEntryName);
                        String[] split = jarEntryName.split("/");
                        dataMap.put("name", split[split.length - 1]);
                        list.add(dataMap);
                    }
                }
            }
        }
        return list;
    }


    public static enum LoadType {
        OUTER, INNER, BOTH;
    }

    /**
     * @param rootPath 文件映射
     * @return 目标文件list
     * @author YYJ
     */
    public static HashMap<String, String> getFilePathMapping(String rootPath) {
        String file_separator = System.getProperty("file.separator");

        if (rootPath.endsWith(file_separator) && rootPath.length() > 1)
            rootPath = StringUtils.removeEnd(rootPath, file_separator);

        File file = new File(rootPath);
        File[] files = file.listFiles();
        HashMap<String, String> map = new HashMap<>();

        assert files != null;
        LinkedList<File> queue = new LinkedList<>(Arrays.asList(files));
        do {
            File poll = queue.poll();
            if (poll != null) {

                File[] listFiles = null;
                if (poll.isDirectory()) {
                    listFiles = poll.listFiles();
                }
                if (listFiles != null && listFiles.length > 0)
                    queue.addAll(Arrays.asList(listFiles));

                if (poll.isFile()) {
                    String path = poll.getPath();
                    String key = path.replace(rootPath, "");

                    if (isWindows())
                        key = key.replaceAll("\\\\", "/");

                    map.put(key, path);
                }
            }
        } while (queue.size() > 0);

        return map;
    }

    public static boolean isWindows() {
        return "\\".equals(System.getProperty("file.separator"));
    }

}
