package com.covilla.common;

/**
 * 数据库PayFlow状态集合
 * 整合微信支付和支付宝支付的需要收银员辨别的操作，除此之外的其他业务错误，统一定义为"FAIL"，并附带错误原因。
 * Created by qmaolong on 2016/12/2.
 */
public enum PayStateEnum {
    newState("NEW", "", "支付未完成", "", "", true, true),
    success("SUCCESS", "支付已成功，不可重复支付", "", "SUCCESS", "TRADE_SUCCESS", false, true),
    fail("FAIL", "", "", "", "", true, true),
    //----------------to be check--------------------
    systemError("SYSTEMERROR", "系统超时", "调用被扫订单结果查询API，查询当前订单状态", "SYSTEMERROR", "ACQ.SYSTEM_ERROR", true, true),
    bankError("BANKERROR", "银行端超时", "立即调用订单支付查询API，查询当前订单的状态", "BANKERROR", "", true, true),
    userPaying("USERPAYING", "用户支付中，需要输入密码", "等待5秒，然后调用订单结果查询API，查询当前订单的状态", "USERPAYING", "WAIT_BUYER_PAY", false, true),
    //-------------------fail----------------------------
    notEnough("NOTENOUGH", "余额不足", "请收银员提示用户更换当前支付的卡", "NOTENOUGH", "ACQ.BUYER_BANKCARD_BALANCE_NOT_ENOUGH", true, false),
    orderPaid("ORDERPAID", "订单已支付", "请确认该订单号是否重复支付，如果是新单，请使用新订单号提交", "ORDERPAID", "ACQ.TRADE_HAS_SUCCESS", false, true),
    orderClosed("ORDERCLOSED", "该订单已关", "商户订单已关闭，请重新下单支付", "ORDERCLOSED", "ACQ.TRADE_HAS_CLOSE", false, false),
    orderReversed("REVOKED", "当前订单已经被撤销", "请提示用户重新支付", "ORDERREVERSED", "", false, false),
    buyerMismatch("BUYER_MISMATCH", "支付帐号错误", "暂不支持同一笔订单更换支付方，请确认支付方是否相同", "BUYER_MISMATCH", "", true, true),
    orderNoUsed("OUT_TRADE_NO_USED", "订单号重复", "请核实商户订单号是否重复提交", "OUT_TRADE_NO_USED", "", true, true),

    revoked("REVOKED", "支付已撤销", "", "REVOKED", "", false, false),
    refund("REFUND", "已转入退款", "", "REFUND", "", false, false);


    private String name;
    private String desc;
    private String optionAdvise;

    private String wxPayCode;
    private String aliPayCode;

    private boolean allowRepay;//是否允許重新支付
    private boolean allowReRollback;//是否允許重新撤銷

    PayStateEnum(String name, String desc, String optionAdvise, String wxPayCode, String aliPayCode, boolean allowRePay, boolean allowReRollback) {
        this.name = name;
        this.desc = desc;
        this.optionAdvise = optionAdvise;
        this.wxPayCode = wxPayCode;
        this.aliPayCode = aliPayCode;
        this.allowRepay = allowRePay;
        this.allowReRollback = allowReRollback;
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

    public String getOptionAdvise() {
        return optionAdvise;
    }

    public void setOptionAdvise(String optionAdvise) {
        this.optionAdvise = optionAdvise;
    }

    public String getWxPayCode() {
        return wxPayCode;
    }

    public void setWxPayCode(String wxPayCode) {
        this.wxPayCode = wxPayCode;
    }

    public String getAliPayCode() {
        return aliPayCode;
    }

    public void setAliPayCode(String aliPayCode) {
        this.aliPayCode = aliPayCode;
    }

    public boolean isAllowRepay() {
        return allowRepay;
    }

    public void setAllowRepay(boolean allowRepay) {
        this.allowRepay = allowRepay;
    }

    public boolean isAllowReRollback() {
        return allowReRollback;
    }

    public void setAllowReRollback(boolean allowReRollback) {
        this.allowReRollback = allowReRollback;
    }

    /**
     * 用微信错误码获取对应平台错误码
     * @param code
     * @return
     */
    public static PayStateEnum findByWXCode(String code){
        for(PayStateEnum temp : PayStateEnum.values()){
            if(temp.getWxPayCode().equals(code)){
                return temp;
            }
        }
        return PayStateEnum.fail;
    }

    /**
     * 用支付宝错误码获取对应平台错误码
     * @param code
     * @return
     */
    public static PayStateEnum findByAliCode(String code){
        for(PayStateEnum temp : PayStateEnum.values()){
            if(temp.getAliPayCode().equals(code)){
                return temp;
            }
        }
        return PayStateEnum.fail;
    }

    public static PayStateEnum findByLocalCode(String code){
        for(PayStateEnum temp : PayStateEnum.values()){
            if(temp.getName().equals(code)){
                return temp;
            }
        }
        return PayStateEnum.fail;
    }
}
