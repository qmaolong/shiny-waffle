package com.covilla.security;

import com.covilla.common.AuthorityEnum;
import com.covilla.common.RoleEnum;
import com.covilla.model.mongo.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户身份证书
 * 保存在session中，spring security会自动读取到线程中
 * 获取证书，只需要从线程中获取即可
 * Created by qmaolong on 2017/3/18.
 */
public class AuthenticationUser implements UserDetails,Serializable {
    public static final long serialVersionUID = -8436677899692325855L;
    private User user;
    private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    //初始化权限
    public AuthenticationUser(User user) {
        this.user = user;
        if (RoleEnum.owner.getCode().equals(user.getRole()) || RoleEnum.manager.getCode().equals(user.getRole())){
            authorities.add(new SimpleGrantedAuthority(AuthorityEnum.MERCHANT.getName()));
        }else if (RoleEnum.superAdmin.getCode().equals(user.getRole()) || RoleEnum.admin.getCode().equals(user.getRole())){
            authorities.add(new SimpleGrantedAuthority(AuthorityEnum.ADMIN.getName()));
            authorities.add(new SimpleGrantedAuthority(AuthorityEnum.MERCHANT.getName()));
        }
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    @Override
    public String getUsername() {
        return user.getName();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
