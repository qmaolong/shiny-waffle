package com.covilla.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qmaolong on 2016/11/22.
 */
public enum  OrderTypeEnum {
    deskOrder(0, "堂食"),
    fastOrder(1, "快餐"),
    takeOut(2, "外卖"),
    openClass(3, "开班"),
    closeClass(4, "交班"),
    otherOrder(5, "非营业订单"),
    chargeOrder(6, "充值订单"),
    returnCard(7, "退卡订单"),
    rollbackOrder(8, "撤销订单");
    private Integer code;
    private String name;

    OrderTypeEnum(Integer code, String name) {
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

    //营业订单号
    public static List<Integer> getBusinessTypes(){
        List<Integer> types = new ArrayList<Integer>();
        types.add(OrderTypeEnum.deskOrder.getCode());
        types.add(OrderTypeEnum.fastOrder.getCode());
        types.add(OrderTypeEnum.takeOut.getCode());
//        types.add(OrderTypeEnum.otherOrder.getCode());
        return types;
    }
    //营业订单
    public static List<OrderTypeEnum> getBusinessTypesEnum(){
        List<OrderTypeEnum> types = new ArrayList<OrderTypeEnum>();
        types.add(OrderTypeEnum.deskOrder);
        types.add(OrderTypeEnum.fastOrder);
        types.add(OrderTypeEnum.takeOut);
//        types.add(OrderTypeEnum.otherOrder);
        return types;
    }

    //流水订单号
    public static List<Integer> getFlowTypes(){
        List<Integer> types = new ArrayList<Integer>();
        types.add(OrderTypeEnum.deskOrder.getCode());
        types.add(OrderTypeEnum.fastOrder.getCode());
        types.add(OrderTypeEnum.takeOut.getCode());
        types.add(OrderTypeEnum.otherOrder.getCode());
        types.add(OrderTypeEnum.chargeOrder.getCode());
        types.add(OrderTypeEnum.returnCard.getCode());
        return types;
    }
    //流水订单
    public static List<OrderTypeEnum> getFlowTypesEnum(){
        List<OrderTypeEnum> types = new ArrayList<OrderTypeEnum>();
        types.add(OrderTypeEnum.deskOrder);
        types.add(OrderTypeEnum.fastOrder);
        types.add(OrderTypeEnum.takeOut);
        types.add(OrderTypeEnum.otherOrder);
        types.add(OrderTypeEnum.chargeOrder);
        types.add(OrderTypeEnum.returnCard);
        return types;
    }

    public static String getNameByCode(Integer code){
        for (OrderTypeEnum orderTypeEnum : OrderTypeEnum.values()){
            if(code.equals(orderTypeEnum.getCode())){
                return orderTypeEnum.getName();
            }
        }
        return null;
    }
}
