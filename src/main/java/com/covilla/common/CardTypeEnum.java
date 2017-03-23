package com.covilla.common;

import com.covilla.util.ValidatorUtil;

/**
 * Created by qmaolong on 2016/9/14.
 */
public enum  CardTypeEnum {
    virtual(0, "虚拟卡"),
    ic(1, "IC卡"),
    mag(2, "磁卡"),
    ming(3, "明卡"),
    coupon(4, "券");


    CardTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    private Integer code;
    private String name;

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
        if(ValidatorUtil.isNull(code)){
            return "";
        }
        for (CardTypeEnum cardTypeEnum : CardTypeEnum.values()){
            if(code.equals(cardTypeEnum.getCode())){
                return cardTypeEnum.getName();
            }
        }
        return "";
    }
}
