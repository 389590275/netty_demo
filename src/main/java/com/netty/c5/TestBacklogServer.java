package com.netty.c5;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiangchijie
 * @date 2021/12/28 10:44 上午
 */
@Slf4j
public class TestBacklogServer {

    public static void main(String[] args) {
        ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer(1);
        System.out.println(buffer);

        ByteBuf buffer1 = UnpooledByteBufAllocator.DEFAULT.buffer();
        System.out.println(buffer1);

        new ServerBootstrap().
                childOption(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 1024).
                // io.netty.buffer.ByteBufUtil.DEFAULT_ALLOCATOR
                        childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .group(new NioEventLoopGroup()).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
//                        ByteBufAllocator allocator = ch.config().getAllocator();
//                        System.out.println(allocator.getClass());
                        ch.pipeline().addLast(new LoggingHandler());
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                            }
                        });
                    }
                }).bind(8080);
    }

}
