package com.netty.c4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @author xiangchijie
 * @date 2021/11/22 2:15 下午
 */
@Slf4j
public class TestPipeline {
    public static void main(String[] args) {
        new ServerBootstrap().group(new NioEventLoopGroup()).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 1.通过channel获取pipeline
                        ChannelPipeline pipeline = ch.pipeline();
                        // 2. 添加handler head-->h1-->h2-—>h4-->h3-->h5-->h6tail
                        pipeline.addLast("h1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("1");
                                ByteBuf buf = (ByteBuf) msg;
                                String name = buf.toString(Charset.defaultCharset());
                                super.channelRead(ctx, name);
                            }
                        });
                        pipeline.addLast("h2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object name) throws Exception {
                                log.debug("2");
                                Student student = new Student(name.toString());
                                // 将数据传递给下一个handler；如果不调用，调用链断开
                                // 方式1.super.channelRead
                                super.channelRead(ctx, student);
                                // 方式2.ctx.fireChannelRead
                                //ctx.fireChannelRead(student);
                            };
                        });
                        // 4/5/6 出站处理器
                        pipeline.addLast("h4", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("4");
                                super.write(ctx, msg, promise);
                            }
                        });

                        pipeline.addLast("h3", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("3，结果{} class:{}", msg, msg.getClass());
                                // 触发出站处理器
                                // 从tail往前找出站处理器
//                                ch.writeAndFlush(ctx.alloc().buffer().writeBytes("server...".getBytes()));
                                // 从当前handler【h3】 开始往前找出站处理器
                                ctx.writeAndFlush(ctx.alloc().buffer().writeBytes("server...".getBytes()));
                            }
                        });

                        pipeline.addLast("h5", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("5");
                                super.write(ctx, msg, promise);
                            }
                        });
                        pipeline.addLast("h6", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("6");
                                super.write(ctx, msg, promise);
                            }
                        });
                    }
                }).bind(new InetSocketAddress(8080));
    }


    @ToString
    static class Student {
        private String name;

        public Student(String name) {
            this.name = name;
        }
    }
}
