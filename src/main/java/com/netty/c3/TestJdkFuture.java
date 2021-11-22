package com.netty.c3;

import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author xiangchijie
 * @date 2021/11/22 11:17 上午
 */
@Slf4j
public class TestJdkFuture {

    public static void main(String[] args) throws Exception {
        // 1. 线程池
        ExecutorService service = Executors.newFixedThreadPool(2);
        // 2. 提交任务
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("执行计算");
                Thread.sleep(1000);
                return 50;
            }
        });
        // 3. 主线程通过future来获取结果
        log.debug("等待结果");
        log.debug("结果:{}", future.get());
        service.shutdown();
    }

}
