package com.covilla.vo.api;

import com.covilla.vo.BaseApiResultMsg;

/**
 * Created by qmaolong on 2016/12/30.
 */
public class ShopVerifyApiMsg extends BaseApiResultMsg {
    private String shopId;
    private String shopName;
    private String authKey;

    public static ShopVerifyApiMsg buildSuccessResult(String shopId, String shopName, String authKey) {
        return new ShopVerifyApiMsg(shopId, shopName, authKey);
    }

    public ShopVerifyApiMsg(String shopId, String shopName, String authKey) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.authKey = authKey;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }
}
