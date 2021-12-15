package com.netty.proto;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;

/**
 * @author xiangchijie
 * @date 2021/12/14 10:30 上午
 */
public class TestRedis {

    /**
     * set name zhangsan
     *
     * *3   数据有三个元素
     * $3   set的长度
     * set
     * $4   name的长度
     * name
     * $8   zhangsan的长度
     * zhangsan
     *
     * @param args
     * @throws InterruptedException
     */

    public static void main(String[] args) throws InterruptedException {
        final byte[] LINE = {13, 10};
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class)
                    .group(worker).handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LoggingHandler());
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            ByteBuf buf = ctx.alloc().buffer();
                            buf.writeBytes("*3".getBytes()).writeBytes(LINE);
                            buf.writeBytes("$3".getBytes()).writeBytes(LINE);
                            buf.writeBytes("set".getBytes()).writeBytes(LINE);
                            buf.writeBytes("$4".getBytes()).writeBytes(LINE);
                            buf.writeBytes("name".getBytes()).writeBytes(LINE);
                            buf.writeBytes("$8".getBytes()).writeBytes(LINE);
                            buf.writeBytes("zhangsan".getBytes()).writeBytes(LINE);
                            ctx.writeAndFlush(buf);
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf buf = (ByteBuf) msg;
                            System.out.println("channelRead :" + buf.toString(Charset.defaultCharset()));
                        }
                    });

                }
            });
            ChannelFuture channelFuture = bootstrap.connect("localhost", 6379);
            channelFuture.sync();
        } finally {
            worker.shutdownGracefully();
        }

    }

}
