package com.nio.c1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author xiangchijie
 * @date 2021/10/25 10:26 上午
 */
public class TestFileChannelTransferTo {

    public static void main(String[] args) {
        try (FileChannel from = new FileInputStream("data.txt").getChannel();
             FileChannel to = new FileOutputStream("to.txt").getChannel();) {
            // 效率高，底层使用零拷贝 最大2g数据，可以分多次传输
            long size = from.size();
            // left代表还剩多少字节
            for (long left = size; left > 0; ) {
                // transferTo 返回传输了多少字节
                left -= from.transferTo(size - left, left, to);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
