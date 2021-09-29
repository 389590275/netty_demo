package com.demo;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author xiangchijie
 * @date 2021/9/29 2:48 下午
 */

@Slf4j
public class Demo2 {

    public static void main(String[] args) {
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            //准备缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(10);
            //从channel读取数据 写到buffer
            while (true) {
                int len = channel.read(buffer);
                if (len == -1) { // 没有内容了
                    break;
                }
                log.debug("11");
                // 打印
                buffer.flip();// 切换读模式
                while (buffer.hasRemaining()) {//是否还有剩余
                    byte b = buffer.get();
                    System.out.println((char) b);
                }
                buffer.clear();// 切换到写模式
            }
        } catch (IOException e) {

        }
    }

}
