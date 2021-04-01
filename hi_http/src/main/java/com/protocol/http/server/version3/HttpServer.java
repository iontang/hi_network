package com.protocol.http.server.version3;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.protocol.http.util.ConfigUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class HttpServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(">>>>>>>>  start HttpServer: ");
        com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(Integer.parseInt(ConfigUtil.getConfig("port"))), 0);
        server.createContext(ConfigUtil.getConfig("urlPath"), new MyHandler());

        server.setExecutor(null); // creates a default executor
        server.start();

        Thread.sleep(Long.MAX_VALUE);
    }

    static class MyHandler implements HttpHandler {

        public void handle(HttpExchange t) throws IOException {
            System.out.println(""+ t.getRemoteAddress());
            try {
                Thread.sleep(6000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String response = "This is the response  <<<<<<<<<<< ";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}

