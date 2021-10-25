package com.demo.c1;

import java.nio.ByteBuffer;

import static com.util.ByteBufferUtil.debugAll;

/**
 * @author xiangchijie
 * @date 2021/9/29 3:55 下午
 */
public class TestBytebufferReadWrite {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 0x61); // a
        debugAll(buffer);
        buffer.put(new byte[]{0x62,0x63,0x64}); // b c d
        debugAll(buffer);
        // 读模式 从0开始
        buffer.flip();
        System.out.println((char)buffer.get());
        debugAll(buffer);
        // 写模式 从position开始
        buffer.compact();
        debugAll(buffer);
        buffer.put(new byte[]{0x65,0x66}); // b c d
        debugAll(buffer);

        // 写模式 从0开始
        buffer.clear();
    }

}
