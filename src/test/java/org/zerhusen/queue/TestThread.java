package org.zerhusen.queue;

import java.util.concurrent.CountDownLatch;

public class TestThread {

    public static void main(String[] args) {
        long l = timeTasks(5, new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        });
        System.out.println("spend time  = [" + l + "]");

    }


    /**
     * @param nThreads 执行的次数
     * @param task     任务
     * @return
     */
    public static long timeTasks(int nThreads, final Runnable task) {
        final CountDownLatch startGat = new CountDownLatch(1);
        final CountDownLatch endGat = new CountDownLatch(nThreads);
        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        startGat.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        task.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    endGat.countDown();
                }
            };
            t.start();
        }
        long start = System.nanoTime();
        startGat.countDown();
        try {
            endGat.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        return end - start;
    }

}
