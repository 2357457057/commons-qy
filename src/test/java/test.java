import com.auth0.jwt.HeaderParams;
import top.yqingyu.common.utils.HttpUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName PACKAGE_NAME.DemoServer
 * @description
 * @createTime 2022年09月13日 23:45:00
 */
public class test {
    public static File file = null;
    public static long l = 0;

    public static void main(String[] args) throws Exception {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(HeaderParams.CONTENT_TYPE,"text/html");
        byte[] bytes = HttpUtil.doGet3("http://localhost:4728/blog/queryAllPagesV2", hashMap, new HashMap<>());
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
        System.out.println(new String(bytes, StandardCharsets.ISO_8859_1));
    }
}
