package com.netty.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @author xiangchijie
 * @date 2021/11/16 3:38 下午
 */
public class HelloClient {

    public static void main(String[] args) throws InterruptedException {
        // 1.启动类
        new Bootstrap()
                //2.添加EventLoop
                .group(new NioEventLoopGroup())
                //3. 选择客户端channel实现
                .channel(NioSocketChannel.class)
                //4. 添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override // 在建立连接后调用
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // String转成ByteBuf
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                            }
                        });
                    }
                })
                //5.连接到服务器
                .connect(new InetSocketAddress(8080))
                .sync() //则塞方法指导连接建立
                .channel()// 连接对象
                .writeAndFlush("hello world")//发送数据
        ;
    }

}
