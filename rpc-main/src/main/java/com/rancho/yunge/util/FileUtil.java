package com.rancho.yunge.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author zgj-18063794
 * @createTime 2019-01-22 11:31
 */
public class FileUtil {

    public static Properties parse(String fileName) throws IOException {
        InputStream resourceAsStream =
                Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        Properties properties = new Properties();
//        properties.load(resourceAsStream);
        return properties;
    }

}
