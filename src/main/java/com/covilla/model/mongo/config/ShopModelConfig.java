package com.covilla.model.mongo.config;

import com.covilla.model.mongo.shop.Shop;

/**
 * Created by qmaolong on 2016/10/17.
 */
public class ShopModelConfig {
    private String name;
    private Shop value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Shop getValue() {
        return value;
    }

    public void setValue(Shop value) {
        this.value = value;
    }
}
