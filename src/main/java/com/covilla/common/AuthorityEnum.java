package com.covilla.common;

/**
 * Created by qmaolong on 2017/3/17.
 */
public enum  AuthorityEnum {
    MERCHANT("ROLE_MERCHANT", "商家后台管理"),
    ADMIN("ROLE_ADMIN", "系统管理");
    private String name;
    private String desc;

    AuthorityEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
