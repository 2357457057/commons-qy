import org.apache.commons.lang3.math.NumberUtils;
import top.yqingyu.common.server$nio.CreateServer;
import top.yqingyu.common.qymsg.MsgTransfer;
import top.yqingyu.common.utils.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

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
        hashMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        hashMap.put("Accept-Encoding", "gzip");
        hashMap.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        hashMap.put("Cache-Control", "max-age=0");
        hashMap.put("Connection", "keep-alive");
        hashMap.put("Cookie", "Hm_lvt_f4f76646cd877e538aa1fbbdf351c548=1684310660,1684386116,1685428288,1686031711; PHPSESSID=87kejbqjpfsb2eo3qgc1mehqe5; Hm_lpvt_f4f76646cd877e538aa1fbbdf351c548=1686033579");
        hashMap.put("Host", "www.ip138.com");
        hashMap.put("Referer", "https://2023.ip138.com/");
        hashMap.put("Sec-Fetch-Dest", "document");
        hashMap.put("Sec-Fetch-Mode", "navigate");
        hashMap.put("Sec-Fetch-Site", "same-site");
        hashMap.put("Sec-Fetch-User", "?1");
        hashMap.put("Upgrade-Insecure-Requests", "1");
        hashMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 Edg/114.0.1823.37");
        hashMap.put("sec-ch-ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Microsoft Edge\";v=\"114\"");
        hashMap.put("sec-ch-ua-mobile", "?0");
        hashMap.put("sec-ch-ua-platform", "Windows");
        byte[] bytes = HttpUtil.doGet3("https://www.ip138.com/iplookup.php?ip=42.192.75.54&action=2", hashMap, new HashMap<>());
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] bytes1 = gunzip.readAllBytes();
        System.out.println(new String(bytes1, StandardCharsets.UTF_8));


    }
}
