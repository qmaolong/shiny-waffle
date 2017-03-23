package com.covilla.model.mongo.setting;

/**
 * Created by qmaolong on 2017/2/23.
 */
public class WxSupport {
    private String appId;
    private String appName;
    private String appSecret;
    private Boolean useSystem;//使用系统配置

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public Boolean isUseSystem() {
        return useSystem;
    }

    public void setUseSystem(Boolean useSystem) {
        this.useSystem = useSystem;
    }
}
