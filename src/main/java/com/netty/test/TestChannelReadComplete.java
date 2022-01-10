package com.netty.test;

import io.netty.buffer.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

/**
 * @author xiangchijie
 * @date 2022/1/3 12:22 下午
 */
public class TestChannelReadComplete {

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        EmbeddedChannel channel = new EmbeddedChannel(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        ByteBuf in = (ByteBuf) msg;
                        byte[] arr = new byte[in.readableBytes()];
                        in.readBytes(arr);
                        System.out.println(new String(arr, Charset.defaultCharset()));
                        countDownLatch.countDown();
//                        ctx.fireChannelRead(msg);
                    }
                });
            }
        } );
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer();
        buf.writeBytes("hello".getBytes(Charset.defaultCharset()));
        channel.writeInbound(buf);
        countDownLatch.await();
        Thread.sleep(1000L);
        buf.writeChar('a');
        String s = ByteBufUtil.hexDump(buf);
        System.out.println(s);
    }


    /**
     * 缓冲区从有数据到无数据时触发一次
     */
    private static void channelReadComplete() {
        EmbeddedChannel channel = new EmbeddedChannel(new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                ByteBuf in = (ByteBuf) msg;
                byte[] arr = new byte[in.readableBytes()];
                in.readBytes(arr);
                System.out.println(new String(arr, Charset.defaultCharset()));
            }

            @Override
            public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                System.out.println("channelReadComplete");
            }
        });
    }

}
