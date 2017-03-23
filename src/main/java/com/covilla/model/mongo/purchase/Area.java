package com.covilla.model.mongo.purchase;

import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by qmaolong on 2017/3/15.
 */
public class Area {
    private String id;
    private String name;
    private ObjectId shopId;
    private List<Supplier> suppliers;//供应商

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

    public ObjectId getShopId() {
        return shopId;
    }

    public void setShopId(ObjectId shopId) {
        this.shopId = shopId;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }
}
