package com.bw.movie.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * Created by xyj on 2017/2/9.
 */
public class PropertiesUtil {

    public static void main(String[] args) {
        try {
            Properties p = getProperties("properties/email_config.properties", "utf-8");
            System.out.println(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties getProperties(String filepath, String charset) throws IOException {
        Properties pro = new Properties();
        Reader reader = null;
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filepath);
            reader = new InputStreamReader(is, Charset.forName(charset));
            pro.load(reader);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return pro;
    }

}
