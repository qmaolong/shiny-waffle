package com.covilla.common;

/**
 * Created by qmaolong on 2016/9/24.
 */
public enum  BusinessTypeEnum {
    charge(1, "充值", "C"),
    withdraw(2, "提现", "W"),
    payOrder(3, "消费", "P"),
    rollbackPay(4, "撤销消费", "UP"),
    rollbackCharge(5, "撤销充值", "UC"),
    makeDiscount(6, "折扣", "D"),
    rollBackDiscount(7, "撤销折扣", "UD"),
    returnCard(8, "退卡", "R"),
    activate(9, "激活", "A"),
    addPoint(10, "积分", "AP"),
    rollbackPoint(11, "撤销积分", "RP");

    private Integer type;
    private String name;
    private String prefix;

    BusinessTypeEnum(Integer type, String name, String prefix) {
        this.type = type;
        this.name = name;
        this.prefix = prefix;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public static String findNameByType(Integer type){
        for (BusinessTypeEnum b : BusinessTypeEnum.values()){
            if(b.getType().equals(type)){
                return b.getName();
            }
        }
        return "";
    }
}
