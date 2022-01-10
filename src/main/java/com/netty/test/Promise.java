package com.netty.test;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;

import java.util.concurrent.ExecutionException;

/**
 * @author xiangchijie
 * @date 2022/1/7 2:57 下午
 */
public class Promise {

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
        promise.sync();
        System.out.println("end");
    }
}
