package com.netty.chat.server;

import com.netty.chat.protocol.MessageCodecSharable;
import com.netty.chat.protocol.ProtocolFrameDecoder;
import com.netty.chat.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
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


                    // 用来判断是不是 读或写的空闲时间过长 读空闲 写空闲 读和写空闲
                    // 5s内如果没有收到channel的数据，会触发一个 IdleState.READER_IDLE 事件
                    pipeline.addLast(new IdleStateHandler(5, 0, 0));
                    // ChannelDuplexHandler 可以作为入站和出站处理器
                    pipeline.addLast(new ChannelDuplexHandler() {
                        // 用来触发特殊事件
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            IdleStateEvent event = (IdleStateEvent) evt;
                            // 触发了读空闲事件
                            if (event.state() == IdleState.READER_IDLE) {
                                log.debug("已经5s没有读到数据了");
                            }
                        }
                    });

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
