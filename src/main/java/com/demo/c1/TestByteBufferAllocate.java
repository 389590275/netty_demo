package com.demo.c1;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import static com.util.ByteBufferUtil.debugAll;


/**
 * @author xiangchijie
 * @date 2021/9/29 4:59 下午
 */
public class TestByteBufferAllocate {

    public static void main(String[] args) {
        // java.nio.HeapByteBuffer 堆内存 读写效率较低
        System.out.println( ByteBuffer.allocate(16));

        // java.nio.DirectByteBuffer 直接内存
        System.out.println(ByteBuffer.allocateDirect(16));
    }

}
