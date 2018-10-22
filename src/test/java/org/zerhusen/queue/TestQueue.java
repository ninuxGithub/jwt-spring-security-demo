package org.zerhusen.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class TestQueue {

    private static final AtomicInteger num = new AtomicInteger(0);

    public static void main(String[] args) {
        BlockingQueue<Product> queue = new LinkedBlockingDeque<Product>(6);
        BlockingQueue<Product> queue2 = new ArrayBlockingQueue<Product>(6);
        Producer p = new Producer(queue, num);
        Consumer c1 = new Consumer(queue);
        Consumer c2 = new Consumer(queue);
        new Thread(p).start();
        new Thread(c1).start();
        new Thread(c2).start();
    }
}

class Product {

    private Integer num;

    public Product(Integer num) {
        this.num = num;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Product{" + "num=" + num + "}";
    }
}

class Producer implements Runnable {
    private final BlockingQueue<Product> queue;
    private final AtomicInteger autoNumber;

    public Producer(BlockingQueue<Product> queue, AtomicInteger autoNumber) {
        this.queue = queue;
        this.autoNumber = autoNumber;
    }


    @Override
    public void run() {
        while (true) {
            try {
                queue.put(makeProduct());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Product makeProduct() {
        return new Product(autoNumber.getAndIncrement());
    }
}


class Consumer implements Runnable {

    private final BlockingQueue<Product> queue;

    public Consumer(BlockingQueue<Product> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

        while (true) {
            try {
                Product product = queue.take();
                consum(product);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void consum(Product product) {
        System.out.println(product);
    }
}
