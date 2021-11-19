package com.nio.c1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.nio.util.ByteBufferUtil.debugAll;

/**
 * 分散读 读取到多个ByteBuffer
 *
 * @author xiangchijie
 * @date 2021/9/30 11:19 上午
 */
public class TestScatteringReads {

    public static void main(String[] args) {
        try (FileChannel channel = new RandomAccessFile("word.txt", "r").getChannel()) {
            ByteBuffer b1 = ByteBuffer.allocate(3);
            ByteBuffer b2 = ByteBuffer.allocate(3);
            ByteBuffer b3 = ByteBuffer.allocate(5);

            channel.read(new ByteBuffer[]{b1,b2,b3});
            b1.flip();
            b2.flip();
            b3.flip();
            debugAll(b1);
            debugAll(b2);
            debugAll(b3);
        } catch (IOException e) {
        }

    }

}
