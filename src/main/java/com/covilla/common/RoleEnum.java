package com.covilla.common;

/**
 * 角色枚举类
 * Created by qmaolong on 2016/8/31.
 */
public enum  RoleEnum {
    superAdmin("super","系统超级管理员"),
    admin("admin", "系统管理员"),
    owner("owner", "商家"),
    manager("manager", "门店管理员"),
    customer("customer", "游客");


    RoleEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String code;
    public String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 根据code获取角色名
     * @param code
     * @return
     */
    public static String findNameByCode(String code){
        for (RoleEnum c : RoleEnum.values()) {
            if (c.code.equals(code)) {
                return c.name;
            }
        }
        return null;
    }
}
