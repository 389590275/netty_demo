package com.netty.c3;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * @author xiangchijie
 * @date 2021/11/22 11:44 上午
 */
@Slf4j
public class TestNettyPromise {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1. 准备EventLoop
        EventLoop eventLoop = new NioEventLoopGroup().next();

        //2. 可以主动创建
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);

        new Thread(() -> {
            //3. 任意一个线程执行计算，向promise填充结果
            System.out.println("开始计算...");
            try {
                Thread.sleep(1000);
                int i = 1 / 0;
                promise.setSuccess(i);
            } catch (Exception e) {
                promise.setFailure(e);
            }
        }).start();

        // 4.接受结果
        log.debug("等待结果...");
        log.debug("结果:{}", promise.get());
    }

}
