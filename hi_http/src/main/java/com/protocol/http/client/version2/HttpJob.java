package com.protocol.http.client.version2;

import com.alibaba.fastjson.JSONObject;
import com.protocol.http.client.version2.task.ConsumerTask;
import com.protocol.http.client.version2.task.ProduceTask;
import com.protocol.http.util.ConfigUtil;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HttpJob {

    public static void main(String[] args) throws InterruptedException {

        System.out.println(">>>>>>>>>>   star HttpJob");
        ConcurrentLinkedQueue<JSONObject> queue = new ConcurrentLinkedQueue();

        String HTTP_URL = "http://localhost:" + ConfigUtil.getConfig("port") + ConfigUtil.getConfig("urlPath");

        int taskNums = 5;
        ExecutorService produceTasksPool = Executors.newFixedThreadPool(taskNums);
        for (int i=0; i<taskNums; i++) {
            ProduceTask task = new ProduceTask(queue, HTTP_URL);
            produceTasksPool.submit(task);
        }

        int numConsumer = 1;
        ExecutorService consumerTaskPool = Executors.newFixedThreadPool(numConsumer);
        for (int i = 0; i < numConsumer; i++) {
            ConsumerTask consumerTask = new ConsumerTask(queue);
            consumerTaskPool.submit(consumerTask);
        }

        while(!consumerTaskPool.awaitTermination(3, TimeUnit.SECONDS)) {
            // 生产者或者消费者死亡，就关闭该进程
        }

        System.out.println(">>>>>>>>>>   end HttpJob");
    }

}
