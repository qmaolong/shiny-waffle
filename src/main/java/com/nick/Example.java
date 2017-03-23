package com.nick;

import com.nick.config.MyErrorPageRegister;
import com.nick.model.User;
import com.nick.repository.mongo.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import java.util.concurrent.TimeUnit;

/**
 * Created by qmaolong on 2017/2/26.
 */
@SpringBootApplication
public class Example {
    @Autowired
    private UserDao repository;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Example.class, args);
    }

    public void run() throws Exception{
        User user = repository.findByName("covilla");
        System.out.println(user);
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.setPort(9000);
        factory.setSessionTimeout(10, TimeUnit.MINUTES);
        factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/notfound.html"));
        return factory;
    }

    @Bean
    public ErrorPageRegistrar errorPageRegistrar(){
        return new MyErrorPageRegister();
    }

}