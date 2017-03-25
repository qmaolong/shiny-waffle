package com.covilla.common;

/**
 * Created by qmaolong on 2016/9/27.
 */
public enum  CardStateEnum {
    newCard(0, "未激活"),
    activated(1, "已激活"),
    frozen(2, "已冻结"),
    used(3, "已使用"),
    disabled(4, "已作废");

    private Integer code;
    private String name;

    CardStateEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getNameByCode(Integer code){
        for (CardStateEnum cardStateEnum : CardStateEnum.values()){
            if(cardStateEnum.getCode() == code){
                return cardStateEnum.getName();
            }
        }
        return "";
    }
}
