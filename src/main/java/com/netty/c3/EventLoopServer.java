package com.netty.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author xiangchijie
 * @date 2021/11/16 5:28 下午
 */
@Slf4j
public class EventLoopServer {

    public static void main(String[] args) {
        //细分2 创建一个独立的EventLoopGroup 用于耗时的handler
        EventLoopGroup group = new DefaultEventLoopGroup();

        new ServerBootstrap()
                //细分1 boss 只负责accept事件 worker 只负责socketChannel上的读写  boss和NioServerSocketChannel绑定 worker和NioSocketChannel绑定
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast("handler1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.debug("handler1 msg:{}", buf.toString(Charset.defaultCharset()));
                                // 将消息传递给下一个handler
                                ctx.fireChannelRead(msg);
                            }
                        });
                        ChannelHandler channelHandler = new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.debug("handler2 msg:{}", buf.toString(Charset.defaultCharset()));
                                ctx.fireChannelRead(buf.toString(Charset.defaultCharset()));
                            }
                        };
                        // 绑定指定group，这里 Channel 和 EventLoop绑定
                        // ch.pipeline().addLast(group, "handler2", channelHandler);

                    }
                })
                .bind(8080);
    }

}
