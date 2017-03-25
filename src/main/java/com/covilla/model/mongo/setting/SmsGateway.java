package com.covilla.model.mongo.setting;

/**
 * Created by qmaolong on 2017/3/3.
 */
public class SmsGateway {
    private String appName;
    private String appKey;
    private String appSecret;
    private Boolean checkCode;
    private String template;
    private String signature;
    private Boolean useSystem;//使用系统配置
    private Integer dayLimit;//每日可发最高上限

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public Boolean getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(Boolean checkCode) {
        this.checkCode = checkCode;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Boolean getUseSystem() {
        return useSystem;
    }

    public void setUseSystem(Boolean useSystem) {
        this.useSystem = useSystem;
    }

    public Integer getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(Integer dayLimit) {
        this.dayLimit = dayLimit;
    }

    public enum  SmsTypeEnum {
        ALIDAYU("alidayu", "阿里大于"),
        CHUANGLAN("sms235", "创蓝");
        private String name;
        private String desc;

        SmsTypeEnum(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
