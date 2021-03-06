package com.netty.c5;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiangchijie
 * @date 2021/12/20 5:40 下午
 */
@Slf4j
public class TestConnectTimeout {

    public static void main(String[] args) {
        // 1.客户端通过.option() 方法配置参数 给SocketChannel配置参数
        // 2.服务器
        // new ServerBootstrap().option() 给 ServerSocketChannel配置参数
        // new ServerBootstrap().channelOption() 给SocketChannel配置参数
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 300)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect("127.0.0.1", 8080);
            future.sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }

}
