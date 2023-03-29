import top.yqingyu.common.utils.StringUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;

public class a {

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("H:/b.java");
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        FileOutputStream fileOutputStream = new FileOutputStream("H:b.ass");
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

        String s = "";

        DateTimeFormatter a = new DateTimeFormatterBuilder()
                .appendPattern("H:mm:ss.SS").toFormatter();


        while ((s = bufferedReader.readLine()) != null) {
            if (StringUtil.startsWith(s, "Dialogue")) {
                String[] split = s.split(",Default,,");
                String s1 = split[0];
                String[] split1 = s1.split(",");
                String time1 = split1[1];
                String time2 = split1[2];
                LocalTime parse1 = LocalTime.parse(time1, a);
                LocalTime localTime1 = parse1.plus(1500, ChronoUnit.MILLIS);
                LocalTime parse2 = LocalTime.parse(time2, a);
                LocalTime localTime2 = parse2.plus(1500, ChronoUnit.MILLIS);
                s = s.replace("Dialogue: 0," + time1, "Dialogue: 0," + a.format(localTime1))
                        .replace(time2 + ",Default,,", a.format(localTime2) + ",Default,,");
            }
            bufferedWriter.write(s);
            bufferedWriter.newLine();
        }

    }

}