package com.netty.c1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.util.List;

/**
 * @author xiangchijie
 * @date 2021/11/16 3:27 下午
 */
public class HelloServer {

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();// 处理连接
        NioEventLoopGroup worker = new NioEventLoopGroup();// 处理读写
        // 1.启动器
        new ServerBootstrap()
                // 2.BossEventLoop , WorkerEventLoop(selector,thread)
                .group(boss, worker)
                // 3.选择服务器的ServerSocketChannel的实现 [BIO NIO AIO]
                .channel(NioServerSocketChannel.class)
                // 4.boss 负责处理连接 worker(child)处理读写 能执行哪些操作(handler)
                .childHandler(
                        // 5. channel代表和客户端进行数据读写的通道Initializer初始化,负责添加别的handler
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override // 客户端连接上来后 执行initChannel
                            protected void initChannel(NioSocketChannel ch) throws Exception {
                                // 添加具体的handler
                                // 将ByteBuf转成String
                                ch.pipeline().addLast(new StringDecoder());
                                // 自定义handler
                                ch.pipeline().addLast(new MessageToMessageDecoder<String>() {
                                    @Override
                                    protected void decode(ChannelHandlerContext channelHandlerContext, String msg, List list) throws Exception {
                                        // channelHandlerContext.fireChannelRead(msg+ "coco");
                                        list.add(msg + "coco");
                                    }
                                });
                                // 自定义handler
                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                    // 读事件
                                    @Override
                                    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
                                        // 打印上一步转换好的字符串
                                        System.out.println(msg);
                                    }
                                });
                            }
                        })
                //7. 绑定监听端口
                .bind(8080);

    }

}
