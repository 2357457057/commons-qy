import top.yqingyu.common.utils.IoUtil;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YYJ
 * @version 1.0.0 * @ClassName PACKAGE_NAME.TestTable
 * @description
 * @createTime 2023年02月19日 03:30:00
 */
public class TestTable {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ConcurrentHashMap<String, String> concurrentDataMap = new ConcurrentHashMap<>();
        concurrentDataMap.put("a","b");
        concurrentDataMap.put("b","b");
        byte[] bytes = IoUtil.objToSerializBytes(concurrentDataMap);
        ConcurrentHashMap concurrentDataMap1 = IoUtil.deserializationObj(bytes, ConcurrentHashMap.class);

        concurrentDataMap1.forEach((a,b)->{
            System.out.println(a.toString() + b.toString());
        });

    }
}
