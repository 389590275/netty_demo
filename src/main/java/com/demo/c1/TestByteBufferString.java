package com.demo.c1;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static com.util.ByteBufferUtil.debugAll;

/**
 * @author xiangchijie
 * @date 2021/9/30 10:51 上午
 */
public class TestByteBufferString {

    public static void main(String[] args) {
        // 1. 字符串转为ByteBuffer
        String hello = "hello";
        test1(hello);
        test2(hello);
        test3(hello);
    }

    private static void test1(String hello){
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put(hello.getBytes());
        // position: [5], limit: [16]
        debugAll(buffer);
    }

    // 2.Charset or StandardCharsets
    private static void test2(String hello){
        // position: [0], limit: [5] 自动切换到读模式【flip】
        ByteBuffer buffer = StandardCharsets.UTF_8.encode(hello);
        debugAll(buffer);

        // decode 只有在读模式中
        String s = StandardCharsets.UTF_8.decode(buffer).toString();
        System.out.println(s);
    }

    // ByteBuffer.wrap
    private static void test3(String hello){
        // position: [0], limit: [5] 自动切换到读模式【flip】
        ByteBuffer buffer = ByteBuffer.wrap(hello.getBytes());
        debugAll(buffer);
    }

}
