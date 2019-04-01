package com.protocol.http.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {

    public static String getConfig(String key) {
        Properties props = new Properties();

        String proFileName = "application.properties";

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream resourceStream = loader.getResourceAsStream(proFileName);
        try {
            props.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props.getProperty(key);
    }

    public static void main(String[] args) {
        System.out.println(ConfigUtil.getConfig("port"));
    }
}
