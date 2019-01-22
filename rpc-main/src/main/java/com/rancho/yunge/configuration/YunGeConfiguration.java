package com.rancho.yunge.configuration;

import com.rancho.yunge.util.FileUtil;

import java.io.IOException;

/**
 * @author zgj-18063794
 * @createTime 2019-01-22 11:19
 */
public class YunGeConfiguration {

    private String configPath = "yunge-rpc.properties";

    public YunGeConfiguration() throws IOException {
        new YunGeConfiguration(configPath);
    }

    public YunGeConfiguration(String configPath) throws IOException {
        this.configPath = configPath;
        FileUtil.parse(configPath);
    }


}
