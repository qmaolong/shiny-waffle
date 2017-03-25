package com.covilla.common;

/**
 * Created by qmaolong on 2016/11/22.
 */
public enum PaymentEnum {
    cash("cash", "现金支付"),
    bank("bank", "银行卡支付"),
    card("card", "会员卡支付"),
    coupon("coupon", "券支付"),
    wxPay("weixin", "微信支付"),
    aliPay("ali", "支付宝支付");

    private String name;
    private String desc;

    PaymentEnum(String name, String desc) {
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
