package com.netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.internal.StringUtil;

/**
 * @author xiangchijie
 * @date 2021/12/10 5:45 下午
 */
public class TestByteBufSlice {

    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        log(buf);
        // 在切片的动作中，没有发生数据复制
        ByteBuf f1 = buf.slice(0, 5);
        f1.retain();
        System.out.println(f1.maxCapacity());
        ByteBuf f2 = buf.slice(5, 5);
        f2.retain();
        System.out.println("释放");
        buf.release();
        log(f1);

        f1.release();
        f2.release();
    }


    private static void log(ByteBuf byteBuf) {
        int length = byteBuf.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder sb = new StringBuilder(rows * 80 * 2).append("read index:").append(byteBuf.readerIndex())
                .append(" write index:").append(byteBuf.writerIndex()).append("cap:").append(byteBuf.capacity()).append(StringUtil.NEWLINE);
        ByteBufUtil.appendPrettyHexDump(sb, byteBuf);
        System.out.println(sb.toString());
    }


}
