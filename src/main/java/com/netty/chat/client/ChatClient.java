package com.netty.chat.client;

import com.google.common.collect.Sets;
import com.netty.chat.message.*;
import com.netty.chat.protocol.MessageCodecSharable;
import com.netty.chat.protocol.ProtocolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xiangchijie
 * @date 2021/12/15 2:53 下午
 */
@Slf4j
public class ChatClient {

    public static void main(String[] args) {
        MessageCodecSharable messageCodec = new MessageCodecSharable();
        NioEventLoopGroup group = new NioEventLoopGroup();
        CountDownLatch WAIT_LOGIN = new CountDownLatch(1);
        AtomicBoolean login = new AtomicBoolean(false);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new ProtocolFrameDecoder());
                    pipeline.addLast(messageCodec);

                    // 3s内如果没有向服务器写数据，会触发一个IdleStateEvent.WRITER_IDLE事件
                    pipeline.addLast(new IdleStateHandler(0, 3, 0));
                    // ChannelDuplexHandler 可以作为入站和出站处理器
                    pipeline.addLast(new ChannelDuplexHandler() {
                        // 用来触发特殊事件
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            IdleStateEvent event = (IdleStateEvent) evt;
                            // 触发了写空闲事件，发送心跳数据
                            if (event.state() == IdleState.WRITER_IDLE) {
                                log.debug("3s没有写数据了自动发个心跳包");
                                ctx.writeAndFlush(new PingMessage());
                            }
                        }
                    });

                    pipeline.addLast("clientHandler", new ChannelInboundHandlerAdapter() {
                        @Override
                        // 连接建立后触发active事件 ，发送登录请求
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            // 负责接受用户在控制台输入，负责向服务器发送各种消息
                            new Thread(() -> {
                                Scanner scanner = new Scanner(System.in);
                                System.out.println("请输入用户名");
                                String username = scanner.nextLine();
                                System.out.println("请输入密码");
                                String password = scanner.nextLine();
                                LoginRequestMessage message = new LoginRequestMessage(username, password);
                                ctx.writeAndFlush(message);
                                System.out.println("等待后续输入");
                                try {
                                    WAIT_LOGIN.await();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                // 登录失败
                                if (!login.get()) {
                                    ctx.channel().close();
                                    return;
                                }
                                while (true) {
                                    System.out.println("===========================");
                                    System.out.println("send [username content]");
                                    System.out.println("gsend [group name content]");
                                    System.out.println("gcreate [group name m1,m2,m3...]");
                                    System.out.println("gmembers [group name]");
                                    System.out.println("gjoin [group name]");
                                    System.out.println("gquit [group name]");
                                    System.out.println("quit");
                                    System.out.println("===========================");
                                    String command = scanner.nextLine();
                                    String[] s = command.split(" ");
                                    switch (s[0]) {
                                        case "send":
                                            ctx.writeAndFlush(new ChatRequestMessage(username, s[1], s[2]));
                                            break;
                                        case "gsend":
                                            ctx.writeAndFlush(new GroupChatRequestMessage(username, s[1], s[2]));
                                            break;
                                        case "gcreate":
                                            HashSet<String> members = Sets.newHashSet(Arrays.asList(s[2].split(",")));
                                            members.add(username);
                                            ctx.writeAndFlush(new GroupCreateRequestMessage(s[1], members));
                                            break;
                                        case "gmembers":
                                            ctx.writeAndFlush(new GroupMembersRequestMessage(s[1]));
                                            break;
                                        case "gjoin":
                                            ctx.writeAndFlush(new GroupJoinRequestMessage(username, s[1]));
                                            break;
                                        case "gquit":
                                            ctx.writeAndFlush(new GroupQuitRequestMessage(username, s[1]));
                                            break;
                                        case "quit":
                                            ctx.channel().close();
                                            return;
                                    }
                                }
                            }, "system in").start();
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            log.debug("msg:{}", msg);
                            if (msg instanceof LoginResponseMessage) {
                                LoginResponseMessage response = (LoginResponseMessage) msg;
                                // 如果登录成功
                                if (response.isSuccess()) {
                                    login.set(true);
                                }
                            }
                            WAIT_LOGIN.countDown();
                        }
                    });
                }
            });
            Channel channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("client error", e);
        } finally {
            group.shutdownGracefully();
        }
    }

}
