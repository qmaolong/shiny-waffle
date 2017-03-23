package com.covilla.model.mongo.organization;

import com.covilla.util.ValidatorUtil;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Created by qmaolong on 2016/9/9.
 */
@Document
public class Section {
    @Field("id")
    private Integer id;
    private String name;
    private List<String> authority;
    @Field("members")
    private List<Clerk> clerks;
    private Double minDiscount;
    private Double specialDiscount;
    @Transient
    private String authorityStr;

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

    public List<String> getAuthority() {
        return authority;
    }

    public void setAuthority(List<String> authority) {
        this.authority = authority;
    }

    public List<Clerk> getClerks() {
        return clerks;
    }

    public void setClerks(List<Clerk> clerks) {
        this.clerks = clerks;
    }

    public Double getMinDiscount() {
        return minDiscount;
    }

    public void setMinDiscount(Double minDiscount) {
        this.minDiscount = minDiscount;
    }

    public Double getSpecialDiscount() {
        return specialDiscount;
    }

    public void setSpecialDiscount(Double specialDiscount) {
        this.specialDiscount = specialDiscount;
    }

    public String getAuthorityStr() {
        if(ValidatorUtil.isNotNull(authority)){
            authorityStr = "";
            for (String temp : authority){
                authorityStr += temp + ",";
            }
            authorityStr = authorityStr.substring(0, authorityStr.length() - 1);
        }
        return authorityStr;
    }

    public void setAuthorityStr(String authorityStr) {
        this.authorityStr = authorityStr;
    }
}
