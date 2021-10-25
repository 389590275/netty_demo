package com.demo.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static com.util.ByteBufferUtil.debugRead;

/**
 * @author xiangchijie
 * @date 2021/10/25 2:57 下午
 */
@Slf4j
public class BlockServer {

    // 使用NIO 理解阻塞模式
    public static void main(String[] args) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(16);

        // 1.创建了服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 2.绑定监听端口
        ssc.bind(new InetSocketAddress(8080));

        // 3. 连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            // 4.accept 阻塞等待客户端连接,SocketChannel用来与客户端通信
            log.debug("connecting...");
            SocketChannel sc = ssc.accept(); // 阻塞方法，线程停止运行
            log.debug("connected...");
            channels.add(sc);
            for (SocketChannel channel : channels) {
                // 5.接收客户端发送的数据
                log.debug("before read...{}", buffer);
                channel.read(buffer);// 则塞
                buffer.flip();
                debugRead(buffer);
                buffer.clear();
                log.debug("after read...{}", buffer);
            }
        }

    }

}
