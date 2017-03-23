package com.covilla.model.mongo.food;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by qmaolong on 2016/9/29.
 */
public class FoodOption {
    @Field("id")
    private String id;
    private String name;
    @Field("price")
    private Double floatPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getFloatPrice() {
        return floatPrice;
    }

    public void setFloatPrice(Double floatPrice) {
        this.floatPrice = floatPrice;
    }
}
