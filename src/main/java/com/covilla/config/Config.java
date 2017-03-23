package com.covilla.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by qmaolong on 2017/2/27.
 */
@ConfigurationProperties("test")
public class Config {
    private String name = new String();

    public String getName() {
        return this.name;
    }
}
