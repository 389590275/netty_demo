package com.netty.c3;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * @author xiangchijie
 * @date 2021/11/22 11:26 上午
 */
@Slf4j
public class TestNettyFuture {

    public static void main(String[] args) throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        // io.netty.util.concurrent.Future
        Future<Integer> future = group.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("执行计算");
                return 70;
            }
        });
        // 3. 主线程通过future来获取结果
//        log.debug("等待结果");
//        log.debug("结果:{}", future.get());

        future.addListener(new GenericFutureListener<Future<? super Integer>>() {
            // 在group线程执行
            @Override
            public void operationComplete(Future<? super Integer> future) throws Exception {
                log.debug("接受结果:{}", future.getNow());
            }
        });
        group.shutdownGracefully();
    }
}
