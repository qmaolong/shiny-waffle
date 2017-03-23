package com.covilla.model.mongo.config;

import com.covilla.model.mongo.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by qmaolong on 2016/8/31.
 */
@Document(collection = "config")
public class ConfigModule extends BaseModel {
    public static final String WX_MENU_CONFIG = "wxMenuConfig";

    private String name;
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
