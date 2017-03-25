package com.covilla.vo.food;

import com.covilla.model.mongo.food.Food;
import org.springframework.data.annotation.Transient;

/**
 * Created by qmaolong on 2016/12/7.
 */
public class FoodImportVo extends Food {
    @Transient
    private String cateId;
    @Transient
    private Boolean pass = true;
    @Transient
    private String noPassReason;

    public Boolean getPass() {
        return pass;
    }

    public void setPass(Boolean pass) {
        this.pass = pass;
    }

    public String getNoPassReason() {
        return noPassReason;
    }

    public void setNoPassReason(String noPassReason) {
        this.noPassReason = noPassReason;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }
}
