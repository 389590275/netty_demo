package com.netty.test;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import static com.nio.util.ByteBufferUtil.debugAll;

/**
 * 多线程版
 *
 * @author xiangchijie
 * @date 2021/11/15 5:39 下午
 */
@Slf4j
public class MultiThreadServer {

    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector boss = Selector.open();
        SelectionKey bossKey = ssc.register(boss, SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));

        // 1. 创建worker
        Worker worker = new Worker("worker-0");
        worker.register();

        while (true) {
            boss.select();
            Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    log.debug("before register {}", sc.getRemoteAddress());
                    // 2. 关联selector
                    sc.register(worker.selector, SelectionKey.OP_READ, null); // 会被 worker.selector.select()阻塞
                    log.debug("after register {}", sc.getRemoteAddress());
                }
            }
        }

    }


    static class Worker implements Runnable {
        private Thread thread;
        private Selector selector;
        private String name;

        private volatile boolean start = false;

        public Worker(String name) {
            this.name = name;
        }

        // 初始化线程
        public void register() throws IOException {
            if (!start) {
                thread = new Thread(this, name);
                thread.start();
                selector = Selector.open();
                start = true;
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    log.debug("select");
                    selector.select();
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel channel = (SocketChannel) key.channel();
                            log.debug("read ");
                            channel.read(buffer);
                            buffer.flip();
                            debugAll(buffer);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
