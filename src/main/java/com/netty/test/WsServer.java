package com.netty.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author xiangchijie
 * @date 2022/1/5 3:11 下午
 */
public class WsServer {

    public static void main(String[] args) {

        new ServerBootstrap()
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        // websocket 基于http协议，所以要有http编解码器 服务端用HttpServerCodec
                        pipeline.addLast(new HttpServerCodec());
                        // 对写大数据流的支持
                        pipeline.addLast(new ChunkedWriteHandler());
                        /**
                         * 我们通常接收到的是一个http片段，如果要想完整接受一次请求的所有数据，我们需要绑定HttpObjectAggregator，然后我们
                         * 就可以收到一个FullHttpRequest-是一个完整的请求信息。
                         *对httpMessage进行聚合，聚合成FullHttpRequest或FullHttpResponse
                         * 几乎在netty中的编程，都会使用到此hanler
                         */
                        pipeline.addLast(new HttpObjectAggregator(1024 * 64));

                        /**
                         * websocket 服务器处理的协议，用于指定给客户端连接访问的路由 : /ws
                         * 本handler会帮你处理一些繁重的复杂的事
                         * 会帮你处理握手动作： handshaking（close, ping, pong） ping + pong = 心跳
                         * 对于websocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
                         */
                        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

                        ch.pipeline().addLast(new SimpleChannelInboundHandler<TextWebSocketFrame>() {
                            protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
                                // 获取客户端传输过来的消息
                                String content = msg.text();
                                ctx.channel().writeAndFlush(new TextWebSocketFrame(content));
                            }
                        });
                    }
                }).bind(8081);
    }


}
