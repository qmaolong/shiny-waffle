package com.covilla.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qmaolong on 2016/11/22.
 */
public enum  OrderStateEnum {
    newOrder(0, "新订单"),
    closedOrder(1, "已关闭订单"),
    payedOrder(2, "已支付订单"),
    refundOrder(3, "退款订单");

    private Integer code;
    private String name;

    OrderStateEnum(Integer code, String name) {
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

    public static List<Integer> getPayedStates(){
        List<Integer> states = new ArrayList<Integer>();
        states.add(OrderStateEnum.payedOrder.getCode());
        return states;
    }
}
