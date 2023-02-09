import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName PACKAGE_NAME.sss
 * @description
 * @createTime 2023年01月05日 00:25:00
 */
public class sss {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 4728);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(new String("aaaaaaaaaaaaaaaaaaaaaaaaaaa").getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
        socket.close();

    }
}
