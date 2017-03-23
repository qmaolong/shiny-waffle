package com.covilla.vo.api;

import com.covilla.vo.BaseApiResultMsg;

/**
 * Created by qmaolong on 2017/1/13.
 */
public class ResultApiMsg extends BaseApiResultMsg {
    private Object result;
    private String shopId;
    private String shopName;

    public ResultApiMsg(Object result, String shopId, String shopName) {
        this.result = result;
        this.shopId = shopId;
        this.shopName = shopName;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
