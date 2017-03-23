package com.covilla.common;

/**
 *修改后编辑lastModifyTime
 * shop =>shop集合中的 id name address tel taste categories cashiers printer printerPolicy ticketPrinter payment
 * food =>所有food集合
 * desk=>shop集合中的desk deskCat
 * user=>shop集合中的users
 * Created by qmaolong on 2016/11/26.
 */
public enum  ModifyBlockEnum {
    shop("shop", "门店相关信息"),
    food("food", "菜品相关信息"),
    desk("desk", "桌台"),
    user("user", "职员");

    private String key;
    private String desc;

    ModifyBlockEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
