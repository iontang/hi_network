package com.protocol.http.client.version2.task;

import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConsumerTask implements Runnable{
    ConcurrentLinkedQueue queue;

    public ConsumerTask(ConcurrentLinkedQueue<JSONObject> queue) {
        this.queue = queue;
    }

    String jsonString = "{\"a\":1,\"b\":\"b_value\"}\n";

    @Override
    public void run() {
        while (true) {
            queue.add(JSONObject.parse(jsonString));
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}

