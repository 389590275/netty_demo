package com.netty.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
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
    }


}
