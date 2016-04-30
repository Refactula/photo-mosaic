package com.refactula.photomosaic.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class IOUtils {

    public static InputStream connectHttp(String url, long byteOffset) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("Range", "bytes=" + byteOffset + "-");
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (200 <= responseCode && responseCode < 300) {
            return connection.getInputStream();
        }
        throw new RuntimeException("Fail to connect: responseCode = " + responseCode);
    }

}
