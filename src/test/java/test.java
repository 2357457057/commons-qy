import top.yqingyu.common.nio$server.CreateServer;
import top.yqingyu.common.qymsg.MsgTransfer;
import top.yqingyu.common.utils.FileUtil;
import top.yqingyu.common.utils.ThreadUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName PACKAGE_NAME.test
 * @description
 * @createTime 2022年09月13日 23:45:00
 */
public class test {
    public static File file = null;
    public static long l = 0;

    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        file = new File("H:/sdk.mkv");
        file.delete();
        File file1 = new File("H:/the.boy.the.mole.the.fox.and.the.horse.2022.dv.2160p.web.h265-naisu.mkv");
        l = file1.length() / 10;
        FileUtil.createSizeFile2(file, file1.length());

        MsgTransfer.init(32,3, ThreadUtil.createQyFixedThreadPool(1,null,null));

        CreateServer
                .createDefault("test")
                .implEvent(aa.class)
                .defaultRouter()
                .listenPort(444)
                .start();

    }
}
