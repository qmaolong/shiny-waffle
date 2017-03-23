package com.covilla.common;

/**
 * 数据备份项
 * Created by qmaolong on 2016/12/10.
 */
public enum  DataItemEnum {
    category("categories", "大小类", "categories", ModifyBlockEnum.shop.getKey()),
    food("food", "菜品", "food", ModifyBlockEnum.food.getKey()),
    unit("units", "单位", "units", ModifyBlockEnum.shop.getKey()),
    taste("taste", "口味", "tastes", ModifyBlockEnum.shop.getKey()),
    organize("users", "组织", "sections", ModifyBlockEnum.user.getKey()),
    ticketForms("ticketForms", "小票格式", "ticketForms", ModifyBlockEnum.shop.getKey()),
    kitchenForms("kitchenForms", "厨打格式", "kitchenForms", ModifyBlockEnum.shop.getKey()),
    controlForms("controlForms", "控菜格式", "controlForms", ModifyBlockEnum.shop.getKey()),
    printSchemes("printSchemes", "厨打方案", "printSchemes", ModifyBlockEnum.shop.getKey());
    private String code;
    private String desc;
    private String localCode;
    private String modifyItem;//修改时涉及的lastModifyTime项

    DataItemEnum(String code, String desc, String localCode, String modifyItem) {
        this.code = code;
        this.desc = desc;
        this.localCode = localCode;
        this.modifyItem = modifyItem;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLocalCode() {
        return localCode;
    }

    public void setLocalCode(String localCode) {
        this.localCode = localCode;
    }

    public String getModifyItem() {
        return modifyItem;
    }

    public void setModifyItem(String modifyItem) {
        this.modifyItem = modifyItem;
    }

    public static DataItemEnum findByCode(String code){
        for (DataItemEnum dataItemEnum : DataItemEnum.values()){
            if(dataItemEnum.getCode().equals(code)){
                return dataItemEnum;
            }
        }
        return null;
    }
}
