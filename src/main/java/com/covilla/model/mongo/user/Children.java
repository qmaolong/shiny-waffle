package com.covilla.model.mongo.user;

import com.covilla.model.mongo.food.Food;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Created by LongLongAgo on 2016/8/25.
 */
@Document
public class Children {
    @Field("id")
    private String id;
    private String name;
    @Transient
    private String parentId;
    @Transient
    private List<Food> foods;

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

    public String getParentId() {
        if(null != parentId){
            return parentId;
        }
        return id.substring(0, 2);
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }
}
