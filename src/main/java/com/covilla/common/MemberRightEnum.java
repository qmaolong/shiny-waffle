package com.covilla.common;

/**
 * Created by qmaolong on 2016/10/31.
 */
public enum MemberRightEnum {
    Root("root", "管理员"),
    Waiter("waiter", "服务员"),
    Cashier("covilla", "收银"),
    Order("order", "点菜下单"),
    PayBill("payBill", "结账"),
    OpenDay("openDay", "开班"),
    CloseDay("closeDay", "交班"),
    OpenDesk("openDesk", "开台"),
    ChangeDesk("changeDesk", "改台"),
    CombineDesk("combineDesk", "并桌"),
    SwitchDesk("switchDesk", "换桌"),
    SplitDesk("splitDesk", "拆桌"),
    LockDesk("lockDesk", "锁台"),
    UnlockDesk("unlockDesk", "解锁"),
    SwitchFood("switchFood", "转菜"),
    MemberCharge("memberCharge", "会员充值"),
    MemberReturn("memberReturn", "会员退卡"),
    Config("config", "参数设置"),
    OpenDrawer("openDrawer", "打开钱箱"),
    GiveFood("giveFood", "赠送品项"),
    ChangePrice("changePrice", "改价"),
    ItemDiscount("itemDiscount", "品项折扣"),
    Discount("discount", "整单折扣"),
    ReturnFood("returnFood", "退菜"),
    CancelOrder("cancelOrder", "取消订单"),
    UnPayBill("unPayBill", "反结账"),
    NoSrvFee("noSrvFee", "免服务费"),
    NoMinPay("noMinPay", "免低消"),
    OtherTrade("otherTrade", "现金收支"),
    FoodTally("foodTally", "品相沽清"),
    CleanDesk("cleanDesk", "清桌"),
    ReminderFood("reminderFood", "催菜/重印"),
    CopyDesk("copyDesk", "桌台复制"),
    activeCard("activeCard", "激活卡/券");

    private String name;

    private String description;

    MemberRightEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
