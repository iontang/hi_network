package com.protocol.http.client.version1;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import com.protocol.http.util.ConfigUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class HttpClient {
    // 池化管理
    private static PoolingHttpClientConnectionManager poolConnManager = null;
    private static CloseableHttpClient httpClient;
    //请求器的配置
    private static RequestConfig requestConfig;

    static {

        try {
            System.out.println("初始化HttpClientTest~~~开始");
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    builder.build());
            // 配置同时支持 HTTP 和 HTPPS
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create().register(
                    "http", PlainConnectionSocketFactory.getSocketFactory()).register(
                    "https", sslsf).build();
            // 初始化连接管理器
            poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // 将最大连接数增加到200，实际项目最好从配置文件中读取这个值
            poolConnManager.setMaxTotal(100);
            // 设置最大路由
            poolConnManager.setDefaultMaxPerRoute(50);
            // 根据默认超时限制初始化requestConfig
//            int socketTimeout = 100;
            int socketTimeout = Integer.MAX_VALUE;
            int connectTimeout = 10000;
            int connectionRequestTimeout = 10000;

            requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(connectionRequestTimeout)
                    .setSocketTimeout(socketTimeout)
                    .setConnectTimeout(connectTimeout).build();
            // 初始化httpClient
            httpClient = getConnection();
            System.out.println("初始化HttpClientTest~~~结束");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }



    public static CloseableHttpClient getConnection() {
        CloseableHttpClient httpClient = HttpClients.custom()
                // 设置连接池管理
                .setConnectionManager(poolConnManager)
                // 设置请求配置
                .setDefaultRequestConfig(requestConfig)
                // 设置重试次数
                .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                .build();
        if (poolConnManager != null && poolConnManager.getTotalStats() != null)
        {
            System.out.println("now client pool "
                    + poolConnManager.getTotalStats().toString());
        }
        return httpClient;
    }


    static class GetThread extends Thread {
        private CloseableHttpClient httpClient;
        private String url;
        private HttpContext context;

        public GetThread(CloseableHttpClient client, String url) {
            this.httpClient = client;
            this.url = url;
            this.context = HttpClientContext.create();
        }

        public void run() {
//            for (int i =0;i<3;i++) {
//                try {
//                    HttpRequestUtilsSimple.httpGet(url, null, 10000);
//                    System.out.println(Thread.currentThread().getName() + " loop:" + i);
//                } catch (URISyntaxException e) {
//                    e.printStackTrace();
//                }
//            }

            System.out.println(Thread.currentThread().getName() + " start");
//            for(int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + " loop:");
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                Thread.sleep(0l);
                String result = EntityUtils.toString(entity, "utf-8");
                System.out.println("result = " + result);
                System.out.println(Thread.currentThread().getName() + " Finished");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {

                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // response.close()是否会释放建立的连接还取决于released这个值
//                    if (response != null) {
//                        System.out.println(Thread.currentThread().getName() + " response.close():");
//                        try {
//                            response.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    if (httpGet != null) {
//                        httpGet.releaseConnection();
//                    }

            }

//            }

        }
    }


    public static void main(String[] args) throws InterruptedException {
//        HttpClientTest.httpGet("https://kyfw.12306.cn/otn/login/init");
//        for(int i = 0; i < 3; i++) {
//            HttpClientTest.httpGet("http://localhost:" + ConfigUtil.getConfig("port") + ConfigUtil.getConfig("urlPath"));
//        }
        String[] urisToGet = {
                "http://localhost:" + ConfigUtil.getConfig("port") + ConfigUtil.getConfig("urlPath"),
                "http://localhost:" + ConfigUtil.getConfig("port") + ConfigUtil.getConfig("urlPath"),
                "http://localhost:" + ConfigUtil.getConfig("port") + ConfigUtil.getConfig("urlPath"),
                "http://localhost:" + ConfigUtil.getConfig("port") + ConfigUtil.getConfig("urlPath"),
                "http://localhost:" + ConfigUtil.getConfig("port") + ConfigUtil.getConfig("urlPath")
        };

        GetThread[] threads = new GetThread[urisToGet.length];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new GetThread(httpClient, urisToGet[i]);
        }

        threads[0].start();
        threads[1].start();
        Thread.sleep(8000L);
        System.out.println(">>>>>>>>>>>>>>>>  after Thread.sleep(8000L)  ");
        System.out.println();
        threads[2].start();

//        Thread.sleep(50000l);
//        System.out.println("----------------  after Thread.sleep(50000l) ---------------");
//        System.out.println();
//        threads[3].start();
//
//        System.out.println("----------------  @@@@@@@@@@@@@@@@@@---------------");
//        System.out.println();
//        Thread.sleep(10000l);
//
//        System.out.println("================= after 10s, all end.");
//        System.out.println();

//        for (Thread tmp : threads) {
//            tmp.start();
//        }

    }

}
