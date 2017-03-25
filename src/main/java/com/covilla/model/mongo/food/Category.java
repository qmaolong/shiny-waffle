package com.covilla.model.mongo.food;

import com.covilla.model.mongo.BaseModel;
import com.covilla.model.mongo.user.Children;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LongLongAgo on 2016/8/25.
 */
@Document
public class Category extends BaseModel{
    @Field("id")
    private String id;
    private List<Children> children = new ArrayList<Children>();
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Children> getChildren() {
        return children;
    }

    public void setChildren(List<Children> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String transIdToString(Integer id){
        if(id>9){
            return id.toString();
        }else {
            return "0" + id;
        }
    }
}
