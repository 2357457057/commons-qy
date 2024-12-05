import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author YYJ
 * @version 1.0.0 * @ClassName PACKAGE_NAME.TestTable
 * @description
 * @createTime 2023年02月19日 03:30:00
 */
public class TestTable {

    static class Time {
        String start;
        String end;
    }

    private boolean ok(Time[] times) {
        int[] ints = new int[2500];
        boolean haveZero = false;
        for (Time time : times) {
            int i = Integer.parseInt(time.start.replace(":", ""));
            int j = Integer.parseInt(time.end.replace(":", ""));
            i -= j;
            if (ints[i] == 0 && haveZero) {
                return true;
            } else if (ints[i] == 0) {
                haveZero = true;
            } else if (ints[i] == i) {
                return true;
            } else {
                ints[i] = i;
            }
        }
        return false;
    }


    private Long getTime(String time) {
        return null;
    }


    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(9);
            byte[] buffer = new byte[1024];
            // 创建一个DatagramPacket对象，用于接收数据
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            while (true) {
                // 接收数据报
                socket.receive(packet);
                byte[] data = packet.getData();
                for (int i = 0; i < data.length;) {
                    String s1 = Integer.toHexString(data[i++]);
                    String s2 = Integer.toHexString(data[i++]);
                    String s3 = Integer.toHexString(data[i++]);
                    String s4 = Integer.toHexString(data[i++]);
                    String s5 = Integer.toHexString(data[i++]);
                    String s6 = Integer.toHexString(data[i++]);
                    System.out.println(s1 + ":" + s2 + ":" + s3 + ":" + s4 + ":" + s5 + ":" + s6);
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
