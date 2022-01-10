package com.netty1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiangchijie
 * @date 2022/1/10 11:16 上午
 */
public class ChannelHandlerTest {

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger count = new AtomicInteger();
        ServerBootstrap serverBootstrap = new ServerBootstrap().channel(NioServerSocketChannel.class).group(new NioEventLoopGroup())
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                        System.out.println(count.getAndIncrement() + " " + " ChannelInitializer handlerAdded");
                        super.handlerAdded(ctx);
                    }

                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        System.out.println(count.getAndIncrement() + " " + "ChannelInitializer initChannel");
                        ch.pipeline().addLast(new ChannelInboundHandler() {

                            @Override
                            public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                                System.out.println(count.getAndIncrement() + " " + "channelRegistered");
                            }

                            @Override
                            public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                                System.out.println(count.getAndIncrement() + " " + "channelUnregistered");
                            }

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                System.out.println(count.getAndIncrement() + " " + "channelActive");
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                System.out.println(count.getAndIncrement() + " " + "channelInactive");
                            }

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(count.getAndIncrement() + " " + "channelRead");
                                ctx.writeAndFlush(Unpooled.copiedBuffer("server", Charset.defaultCharset()));
                                ctx.fireChannelRead("");
                            }

                            @Override
                            public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                                System.out.println(count.getAndIncrement() + " " + "channelReadComplete");
                            }

                            @Override
                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                System.out.println(count.getAndIncrement() + " " + "userEventTriggered");
                            }

                            @Override
                            public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
                                System.out.println(count.getAndIncrement() + " " + "channelWritabilityChanged");
                            }

                            @Override
                            public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                                System.out.println(count.getAndIncrement() + " " + "handlerAdded " + ctx);
                            }

                            @Override
                            public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
                                System.out.println(count.getAndIncrement() + " " + "handlerRemoved");
                            }

                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                System.out.println(count.getAndIncrement() + " " + "exceptionCaught");
                            }
                        });


                    }
                });
        ChannelFuture channelFuture = serverBootstrap.bind(8080);
        channelFuture.sync();
        channelFuture.channel().closeFuture().sync();
    }

}
