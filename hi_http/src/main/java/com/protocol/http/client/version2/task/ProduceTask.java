package com.protocol.http.client.version2.task;

import com.alibaba.fastjson.JSONObject;
import com.protocol.http.client.version2.httpservice.HttpService;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.HttpContext;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ProduceTask implements Runnable{

    private ConcurrentLinkedQueue<JSONObject> queue;
    private HttpContext context;
    private String HTTP_URL;



    public ProduceTask(ConcurrentLinkedQueue<JSONObject> queue, String url) {
        this.queue = queue;
        this.HTTP_URL = url;
        this.context = HttpClientContext.create(); // 每个线程都维护一个httpContext，而不是每个连接
    }



    @Override
    public void run() {

        while (true) {

            while (!queue.isEmpty()) {
                HttpService.httpPostSSL(HTTP_URL, queue.poll(), 6000, context);
            }
        }

    }

}
