package com.covilla.vo.card;

import com.covilla.model.mongo.card.Card;

/**
 * Created by qmaolong on 2016/11/13.
 */
public class CardImportVo extends Card {
    private Boolean pass = true;
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
}
