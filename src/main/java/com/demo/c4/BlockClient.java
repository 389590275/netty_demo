package com.demo.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author xiangchijie
 * @date 2021/10/25 3:05 下午
 */
public class BlockClient {

    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost", 8888));
        System.out.println("waiting...");
    }

}
