package com.nio.c1;

import java.nio.ByteBuffer;

import static com.nio.util.ByteBufferUtil.debugAll;

/**
 * @author xiangchijie
 * @date 2021/9/30 10:39 上午
 */
public class TestByteBufferRead {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.flip();
//        testRewind(buffer);
//        testMarkAndReset(buffer);
        testGet(buffer);
    }

    //rewind  从头开始读
    private static void testRewind(ByteBuffer buffer) {
        buffer.get(new byte[4]);
        debugAll(buffer);
        buffer.rewind();
        System.out.println((char) buffer.get());
    }

    // mark and rest
    // mark 做一个标记 记录position位置 reset是将position从知道mark的位置
    private static void testMarkAndReset(ByteBuffer buffer) {
        System.out.println((char)buffer.get());
        System.out.println((char)buffer.get());
        buffer.mark(); // 加标记索引二
        System.out.println((char)buffer.get());
        System.out.println((char)buffer.get());
        buffer.reset();// 将position重置到索引2
        System.out.println((char)buffer.get());
        System.out.println((char)buffer.get());
    }

    // get(i) 不会改变读索引位置
    private static void testGet(ByteBuffer buffer){
        System.out.println((char)buffer.get(3));
        debugAll(buffer);
    }

}
