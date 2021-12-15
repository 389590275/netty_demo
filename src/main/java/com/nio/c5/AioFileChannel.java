package com.nio.c5;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static com.nio.util.ByteBufferUtil.debugAll;

/**
 * @author xiangchijie
 * @date 2021/11/16 2:51 下午
 */
@Slf4j
public class AioFileChannel {

    public static void main(String[] args) throws IOException, InterruptedException {

        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get("word.txt"), StandardOpenOption.READ)) {
            // 参数1 ByteBuffer
            // 参数2 读取的起始位置
            // 参数3 附件
            // 参数4 回调方法
            ByteBuffer buffer = ByteBuffer.allocate(16);
            log.debug("read begin...");
            channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    log.debug("read completed...{}", result);
                    attachment.flip();
                    debugAll(attachment);
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                }
            });
            System.in.read();
            log.debug("read end...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
