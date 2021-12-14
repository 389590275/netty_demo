package com.netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.internal.StringUtil;


/**
 * @author xiangchijie
 * @date 2021/11/22 3:18 下午
 */
public class TestByteBuf {

    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
        buffer.writeBytes(new byte[]{1,2,3,4});
        buffer.writeInt(5);
        buffer.writeInt(6);
        log(buffer);

        System.out.println(buffer.readByte());
        System.out.println(buffer.readByte());
        System.out.println(buffer.readByte());
        System.out.println(buffer.readByte());
        log(buffer);

        buffer.markReaderIndex();
        System.out.println(buffer.readInt());
        log(buffer);

        buffer.resetReaderIndex();
        log(buffer);

    }

    public static void main1() {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        System.out.println(buf.getClass());
        log(buf);//PooledUnsafeDirectByteBuf(ridx: 0, widx: 0, cap: 256)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            sb.append("a");
        }
        buf.writeBytes(sb.toString().getBytes());
        log(buf);//PooledUnsafeDirectByteBuf(ridx: 0, widx: 300, cap: 512)

    }


    public static void log(ByteBuf byteBuf) {
        int length = byteBuf.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder sb = new StringBuilder(rows * 80 * 2).append("read index:").append(byteBuf.readerIndex())
                .append(" write index:").append(byteBuf.writerIndex()).append("cap:").append(byteBuf.capacity()).append(StringUtil.NEWLINE);
        ByteBufUtil.appendPrettyHexDump(sb, byteBuf);
        System.out.println(sb.toString());
    }

}
