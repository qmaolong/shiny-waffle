package com.covilla.model.mongo.shop;

import com.covilla.model.mongo.BaseModel;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by qmaolong on 2016/9/7.
 */
public class Desk extends BaseModel {
    @Field("id")
    private Integer id;
    private String name;
    private String category;
//    private Double price = 0.0;
    private Double minPay = 0.0;
    private Double srvFee = 0.0;
    private Integer parentId;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
