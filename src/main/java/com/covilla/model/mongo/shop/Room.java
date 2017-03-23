package com.covilla.model.mongo.shop;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Created by qmaolong on 2016/9/7.
 */
public class Room {
    @Field("id")
    private Integer id;
    private String name;
    @Field("desk")
    private List<Desk> desks;

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

    public List<Desk> getDesks() {
        return desks;
    }

    public void setDesks(List<Desk> desks) {
        this.desks = desks;
    }
}
