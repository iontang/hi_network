package com.cpu.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ConsumeCpu {
    public static int count = 0;
    public static Counter counter = new Counter();
    public static AtomicInteger atomicInteger = new AtomicInteger(0);
    volatile public static int countVolatile = 0;

    private static Integer num = new Integer(0);
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        // 保证所有线程执行完毕.
        final CountDownLatch cdl = new CountDownLatch(10);
        for (int i=0; i<100;i++) {
            int finalI = i;
            new Thread() {
                public void run() {
                    while (true) {
                        try {
                            lock.lock();
                            num ++;
                            System.out.println(finalI +" num = " + num);
                        } catch (Exception ex) {}
                        finally {
                            lock.unlock();
                        }
                    }

                }
            }.start();
        }

        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class Counter {

    private int value;

    public synchronized int getValue() {
        return value;
    }

    public synchronized int increment() {
        return value++;
    }

    public synchronized int decrement() {
        return --value;
    }
}
