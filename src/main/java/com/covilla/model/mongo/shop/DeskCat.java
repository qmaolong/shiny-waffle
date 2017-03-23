package com.covilla.model.mongo.shop;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by qmaolong on 2016/9/7.
 */
public class DeskCat {
    @Field("id")
    private Integer id;
    private String name;
//    private Double price = 0.0;
    private Double minPay = 0.0;
    private Double srvFee = 0.0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }*/

    public Double getMinPay() {
        return minPay;
    }

    public void setMinPay(Double minPay) {
        this.minPay = minPay;
    }

    public Double getSrvFee() {
        return srvFee;
    }

    public void setSrvFee(Double srvFee) {
        this.srvFee = srvFee;
    }
}
