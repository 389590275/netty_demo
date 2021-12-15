package com.netty.chat.server;

import com.netty.chat.protocol.MessageCodecSharable;
import com.netty.chat.protocol.ProtocolFrameDecoder;
import com.netty.chat.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiangchijie
 * @date 2021/12/15 12:01 下午
 */
@Slf4j
public class ChatServer {

    public static void main(String[] args) {
        MessageCodecSharable messageCodec = new MessageCodecSharable();
        LoggingHandler loggingHandler = new LoggingHandler();

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new ProtocolFrameDecoder());
                    pipeline.addLast(loggingHandler);
                    pipeline.addLast(messageCodec);
                    pipeline.addLast(new LoginRequestMessageHandler());
                    pipeline.addLast(new ChatRequestMessageHandler());
                    pipeline.addLast(new GroupCreateRequestMessageHandler());
                    pipeline.addLast(new GroupJoinRequestMessageHandler());
                    pipeline.addLast(new GroupQuitRequestMessageHandler());
                    pipeline.addLast(new GroupMembersRequestMessageHandler());
                    pipeline.addLast(new GroupChatRequestMessageHandler());
                    pipeline.addLast(new QuitHandler());
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }


}
