package com.covilla.model.mongo.organization;

import com.covilla.util.ValidatorUtil;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Created by qmaolong on 2016/9/9.
 */
public class Clerk {
    @Field("id")
    private Integer id;
    private String name;
    private String password;
    private Integer parentId;
    private List<String> authority;
    @Transient
    private String authorityStr;
    private String cardId;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<String> getAuthority() {
        return authority;
    }

    public void setAuthority(List<String> authority) {
        this.authority = authority;
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

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
