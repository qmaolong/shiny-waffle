package com.nick.config;

import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.ErrorPageRegistry;
import org.springframework.http.HttpStatus;

/**
 * Created by qmaolong on 2017/2/27.
 */
public class MyErrorPageRegister implements ErrorPageRegistrar {
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        registry.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/400"));
        registry.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/404"));
        registry.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/500"));
    }
}
