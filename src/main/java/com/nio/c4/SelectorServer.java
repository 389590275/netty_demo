package com.nio.c4;

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
 * @author xiangchijie
 * @date 2021/10/25 3:37 下午
 */
@Slf4j
public class SelectorServer {

    public static void main(String[] args) throws IOException {
        // 1. 创建爱你selector 管理多个channel
        Selector selector = Selector.open();

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        // 2. 建立selector和channel的联系（注册）
        // selectionKey 就是将来事件发生后，通过它可以知道事件和那个channel发生的事件
        SelectionKey sscKey = ssc.register(selector, 0, null);
        // key只关注accept事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("register key:{}", sscKey);

        ssc.bind(new InetSocketAddress(8888));
        while (true) {
            // 3.select方法，没有触发关注的事件就会阻塞
            // select在事件未处理时，他不会阻塞【水平触发 ， 会一直提示有事件】
            selector.select();
            // 4.处理事件 所有可用的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                log.debug("register key:{}", key);
                // 5. 区分事件类型
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);

                    // 为socketChannel注册事件
                    ByteBuffer buffer = ByteBuffer.allocate(16); // attachment 附件
                    // 将一个ByteBuffer 作为附件关联到selectionKey上
                    SelectionKey scKey = sc.register(selector, 0, buffer);

                    scKey.interestOps(SelectionKey.OP_READ);
                    log.debug("{}", sc);
                } else if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 获取selectionKey上关联的附件
                    ByteBuffer buffer = (ByteBuffer) key.attachment();

                    int read = channel.read(buffer);
                    // 连接断开时 会受到一个读事件，但是没有数据
                    if (read == -1) {
                        key.cancel();
                    } else {
                        split(buffer);
                        if (buffer.position() == buffer.limit()) {
                            ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                            buffer.flip();
                            newBuffer.put(buffer);
                            key.attach(newBuffer);
                        }
                        continue;
                    }
                }
                // 需要手动删除
                iterator.remove();
            }
        }
    }

    /**
     * 实现
     *
     * @param source
     */
    private static void split(ByteBuffer source) {
        // 读模式
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i) == '\n') {
                int len = i + 1 - source.position();
                // 把这条完整消息存入新的 ByteBuffer
                ByteBuffer target = ByteBuffer.allocate(len);
                byte[] bytes = new byte[len];
                source.get(bytes);
                target.put(bytes);
                debugAll(target);
            }
        }

        // 切换写模式
        source.compact();
    }

    // SelectionKey 事件类型
    // accept - 会在有连接请求时触发
    // connect - 是客户端，连接建立后触发
    // read - 可读事件
    // write - 可写事件

}
