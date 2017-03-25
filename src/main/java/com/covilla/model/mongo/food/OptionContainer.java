package com.covilla.model.mongo.food;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qmaolong on 2016/9/29.
 */
public class OptionContainer {
    @Field("id")
    private Integer id;
    private String name;
    private Boolean forceSelect;
    private List<FoodOption> options = new ArrayList<FoodOption>();

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

    public List<FoodOption> getOptions() {
        return options;
    }

    public void setOptions(List<FoodOption> options) {
        this.options = options;
    }

    public Boolean getForceSelect() {
        return forceSelect;
    }

    public void setForceSelect(Boolean forceSelect) {
        this.forceSelect = forceSelect;
    }
}
