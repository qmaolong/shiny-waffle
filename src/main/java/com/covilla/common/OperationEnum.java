package com.covilla.common;

/**
 * Created by qmaolong on 2016/9/13.
 */
public enum OperationEnum {
    add("add", "新增"),
    edit("edit", "编辑"),
    delete("del", "删除"),
    sort("sort", "排序");

    String code;
    String name;

    OperationEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getNameByCode(String code){
        for (OperationEnum operationEnum : OperationEnum.values()){
            if (operationEnum.getCode().equals(code)){
                return operationEnum.getName();
            }
        }
        return "";
    }
}
