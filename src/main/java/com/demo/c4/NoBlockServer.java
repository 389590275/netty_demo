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
public class NoBlockServer {

    // 使用NIO 理解阻塞模式
    public static void main(String[] args) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(16);

        // 1.创建了服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false); // ServerSocketChannel.accept 非阻塞
        // 2.绑定监听端口
        ssc.bind(new InetSocketAddress(8080));

        // 3. 连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            // 4.accept 阻塞等待客户端连接,SocketChannel用来与客户端通信
            SocketChannel sc = ssc.accept();
            if (sc != null) {
                log.debug("connected...{}",sc);
                sc.configureBlocking(false); // 非阻塞 SocketChannel.read
                channels.add(sc);
            }
            for (SocketChannel channel : channels) {
                // 5.接收客户端发送的数据
                int read = channel.read(buffer);// 非则塞
                if (read > 0) {
                    buffer.flip();
                    debugRead(buffer);
                    buffer.clear();
                    log.debug("after read...{}", channel);
                }
            }
        }

    }

}
