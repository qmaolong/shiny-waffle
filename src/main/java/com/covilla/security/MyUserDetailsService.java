package com.covilla.security;

/**
 * Created by qmaolong on 2017/3/17.
 */

import com.covilla.model.mongo.user.User;
import com.covilla.service.user.UserService;
import com.covilla.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        User user = userService.findByName(username);
        if (ValidatorUtil.isNull(user)){
            throw new UsernameNotFoundException("用户未找到");
        }
        return new AuthenticationUser(user);
    }
}
