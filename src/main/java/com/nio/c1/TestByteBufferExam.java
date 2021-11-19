package com.nio.c1;

import java.nio.ByteBuffer;

import static com.nio.util.ByteBufferUtil.debugAll;

/**
 * 简单的粘包 半包
 *
 * @author xiangchijie
 * @date 2021/9/30 11:27 上午®
 */
public class TestByteBufferExam {

    /**
     * 发送三条bytebuffer
     * Hello,world\n
     * I'm zhangsan\n
     * How are you?\n
     * <p>
     * 收到两个bytebuffer （粘包，半包）
     * Hello,world\nI'm zhangsan\nHo
     * w are you?\n
     * <p>
     * 还原成三个bytebuffer
     */
    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);
        debugAll(source);
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

}
