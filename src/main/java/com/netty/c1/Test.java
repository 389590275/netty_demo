package com.netty.c1;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xiangchijie
 * @date 2021/11/16 4:10 下午
 */
public class Test {
    int x = 0;
    boolean v = false;
    CountDownLatch countDownLatch = new CountDownLatch(1);

    public void writer() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        x = 42;
        v = true;
    }

    public void reader() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (v && x != 42) {
            System.out.println(0);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service1 = Executors.newFixedThreadPool(100);
        ExecutorService service2 = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 10000000; i++) {
            Test test = new Test();
            service2.execute(test::writer);
            service1.execute(test::reader);
            Thread.sleep(10);
            test.countDownLatch.countDown();
        }
        service1.shutdown();
        service2.shutdown();
    }

}
