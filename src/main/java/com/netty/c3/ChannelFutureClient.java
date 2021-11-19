package com.netty.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author xiangchijie
 * @date 2021/11/19 4:58 下午
 */
@Slf4j
public class ChannelFutureClient {

    public static void main(String[] args) throws InterruptedException, IOException {
        // 带有Future Promise 的类型都是和异步方法配套使用，用来处理结果
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override // 在建立连接后调用
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // String转成ByteBuf
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                //1. 连接到服务器 异步非阻塞的main发起了调用 ，真正执行connect的是nio线程
                .connect(new InetSocketAddress(8080));
        // 如何获取到正确的Channel对象【完成建立连接的Channel】

        // 2.1 使用sync方法同步处理结果
        channelFuture.sync();//阻塞当前线程，直到nio线程连接建立完毕
        Channel channel = channelFuture.channel();
        log.debug("channelName :{}", channel);
        channel.writeAndFlush("hello world");

        // 2.2 使用addListener方法异步处理结果
        // 在nio线程连接建立好之后，会调用operationComplete
//        channelFuture.addListener((ChannelFutureListener) (ChannelFuture future) -> {
//            Channel channel = future.channel();
//            log.debug("channelName {}", channel);
//            channel.writeAndFlush("hello world");
//        });
    }

}
