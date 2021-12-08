package com.netty.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @author xiangchijie
 * @date 2021/11/19 5:34 下午
 */
@Slf4j
public class CloseFutureClient {


    public static void main(String[] args) throws InterruptedException, IOException {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        // 带有Future Promise 的类型都是和异步方法配套使用，用来处理结果
        ChannelFuture channelFuture = new Bootstrap()
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override // 在建立连接后调用
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        // String转成ByteBuf
                        ch.pipeline().addLast(new StringEncoder());

                    }
                })
                //1. 连接到服务器 异步非阻塞的main发起了调用 ，真正执行connect的是nio线程
                .connect(new InetSocketAddress(8080));

        Channel channel = channelFuture.sync().channel();
        log.debug("channelName:{}", channel);
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if ("q".equals(line)) {
                    channel.close(); //异步操作
                    break;
                }
                channel.writeAndFlush(line);
            }
        }, "input").start();

        // 获取 ChannelFuture 对象 1)同步处理关闭  2）异步处理关闭
        ChannelFuture closeFuture = channel.closeFuture();
        //1. channelFuture.sync();
        //2.addListener
        closeFuture.addListener((ChannelFutureListener) future -> {
            log.debug("channelFuture operationComplete channel:{}", future.channel());
            // 优雅结束 拒绝接受新的任务，现有任务执行完 关闭线程
            eventLoopGroup.shutdownGracefully();
        });

    }


}
