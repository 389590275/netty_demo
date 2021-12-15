package com.netty.chat.protocol;

import com.netty.chat.message.LoginRequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author xiangchijie
 * @date 2021/12/15 10:52 上午
 */
public class TestMessageCodec {

    public static void main(String[] args) throws Exception {
        // LengthFieldBasedFrameDecoder 会保存一些状态数据，是线程不安全
        // 解决粘包 半包
        EmbeddedChannel channel = new EmbeddedChannel(
                // 帧编解码器 length字段偏移量12 length长度4
                new LengthFieldBasedFrameDecoder(65535, 12, 4, 0, 0),
                new LoggingHandler(),
                new MessageCodec()
        );
        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
        // 测试encode
        // channel.writeOutbound(message);
        // 测试decode

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, message, buf);

        // 粘包半包测试
        ByteBuf buf1 = buf.slice(0, 100);
        ByteBuf buf2 = buf.slice(100, buf.readableBytes() - 100);

        // slice 是零拷贝
        buf1.retain();
        // 入站  writeInbound会调用 ByteBuf.release
        channel.writeInbound(buf1);
        // 如果没有 buf1.retain() , 这里buf 已经被释放了
        channel.writeInbound(buf2);
    }

}
