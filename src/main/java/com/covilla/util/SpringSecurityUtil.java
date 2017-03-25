package com.covilla.util;

import com.covilla.model.mongo.user.User;
import com.covilla.security.AuthenticationUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Spring Security工具类
 * Created by qmaolong on 2017/3/20.
 */
public class SpringSecurityUtil {

    public static User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ValidatorUtil.isNull(authentication) || !(authentication.getPrincipal() instanceof AuthenticationUser))
            return null;
        AuthenticationUser authenticationUser = (AuthenticationUser) authentication.getPrincipal();
        if (ValidatorUtil.isNull(authenticationUser))
            return null;
        return authenticationUser.getUser();
    }
}
