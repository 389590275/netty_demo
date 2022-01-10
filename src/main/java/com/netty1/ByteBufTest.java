package com.netty1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * @author xiangchijie
 * @date 2022/1/10 10:38 上午
 */
public class ByteBufTest {


    public static void main(String[] args) {
        ByteBuf buf = testUnPooledHeapByteBuf();
        buf.release();
        buf.writeInt(1);
        System.out.println(buf.readInt());
    }

    public static ByteBuf testPooledHeapByteBuf() {
        ByteBuf buf = ByteBufAllocator.DEFAULT.heapBuffer();
        System.out.println(buf);
        return buf;
    }

    public static ByteBuf testPooledDirectByteBuf() {
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer();
        System.out.println(buf);
        return buf;

    }

    public static ByteBuf testUnPooledHeapByteBuf() {
        ByteBuf buf = Unpooled.buffer();
        System.out.println(buf);
        return buf;

    }

    public static ByteBuf testUnPooledDirectByteBuf() {
        ByteBuf buf = Unpooled.directBuffer();
        System.out.println(buf);
        return buf;

    }

    public static void testByteBufUtil() {
        ByteBuf buf = Unpooled.copiedBuffer("sunshenghao", Charset.defaultCharset());
        String dump = ByteBufUtil.hexDump(buf);
        System.out.println(dump);
    }

}
