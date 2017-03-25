package com.covilla.model.mongo.food;

import com.covilla.model.mongo.BaseModel;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by qmaolong on 2016/8/30.
 */
public class Unit extends BaseModel{
    @Field("id")
    private Integer id;
    private String name;

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
}
