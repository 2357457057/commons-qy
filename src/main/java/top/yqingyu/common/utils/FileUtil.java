package top.yqingyu.common.utils;

import top.yqingyu.common.exception.IllegalFileCreateException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.StandardOpenOption;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.utils.FileUtil
 * @description
 * @createTime 2023年01月04日 22:02:00
 */

public class FileUtil {


    /**
     * 创建一个固定大小的空文件，如果文件已经存在则会抛出异常
     *
     * @version 1.0.0
     * @description
     */
    @Deprecated
    public static void createSizeFile(File file, long size) throws IOException {
        if (file.exists())
            throw new FileAlreadyExistsException("文件已存在");
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs())
            throw new IllegalFileCreateException("无法创建文件-父文件夹无法创建");
        if (!file.createNewFile())
            throw new IllegalFileCreateException("无法创建文件-文件创建失败");
        FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.WRITE);
        fileChannel.write(ByteBuffer.allocateDirect(1), size - 1);
        fileChannel.close();
    }

    /**
     * 创建一个固定大小的空文件，如果文件已经存在则会抛出异常
     *
     * @version 1.0.0
     * @description
     */
    public static void createSizeFile2(File file, long size) throws IOException {
        if (file.exists())
            throw new FileAlreadyExistsException("文件已存在");
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs())
            throw new IllegalFileCreateException("无法创建文件-父文件夹无法创建");
        if (!file.createNewFile())
            throw new IllegalFileCreateException("无法创建文件-文件创建失败");
        RandomAccessFile rw = new RandomAccessFile(file, "rw");
        rw.setLength(size);
        rw.close();
    }


    public static void transFile(File file, long position, long length, WritableByteChannel targetChannel) throws IOException {

        FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ);

        long lengthTmp1 = length;
        long lengthTmp2 = 0;
        long lengthTmp3 = 0;
        long positionTmp = position;
        do {
            lengthTmp2 = fileChannel.transferTo(positionTmp, lengthTmp1, targetChannel);
            positionTmp += lengthTmp2;
            lengthTmp1 -= lengthTmp2;
            lengthTmp3 += lengthTmp2;
        } while (lengthTmp3 != length);
    }

    public static void transFile2(File file, long position, long length, WritableByteChannel targetChannel) throws IOException {
        FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
        long l = fileChannel.transferTo(position, length, targetChannel);
        System.out.println(l);
    }

/*    *//**
     * 文件唯一ID
     * 文件名
     * 文件总长度
     * 切分次数
     * 切分长度
     * 
     * 当前切分文件次序
     * *//*
    public static void main(String[] args) throws IOException {

        File file = new File("H:/the.boy.the.mole.the.fox.and.the.horse.2022.dv.2160p.web.h265-naisu.mkv");
        long length = file.length();
        MsgTransfer.init(32, 3, ThreadUtil.createQyFixedThreadPool(1, null, null));
        long times = length / 10;

        ConcurrentLinkedQueue<String> position = new ConcurrentLinkedQueue<>();

        for (int i = 0; i <= 10; i++) {
            String length1 = MsgTransfer.getLength(i);
            System.out.println(length1);
            position.add(length1);
        }

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    while (position.size() > 0) {
                        String poll = position.poll();
                        if (poll != null) {
                            SocketChannel channel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 444));
                            IoUtil.writeBytes(channel, poll.getBytes());
                            int p = Integer.parseInt(poll, 32);
                            FileUtil.transFile2(file, Math.max(p * times - 1, 0), times, channel);
                            Thread.sleep(2);
                            channel.close();
                        }

                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }*/
}
