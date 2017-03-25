package com.covilla.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by qmaolong on 2017/3/24.
 */
@Configuration
public class StaticResourceConfigurer extends WebMvcConfigurerAdapter {
    private String[] locations = {"file:/var/static/"};
    private String[] pattern = {"/static/**"};
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(getPattern()).addResourceLocations(getLocations());
        registry.addResourceHandler("/asset/**").addResourceLocations("classpath:/asset/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/favicon.ico");
        super.addResourceHandlers(registry);
    }
    public String[] getLocations() {
        return locations;
    }
    public void setLocations(String[] locations) {
        this.locations = locations;
    }
    public String[] getPattern() {
        return pattern;
    }
    public void setPattern(String[] pattern) {
        this.pattern = pattern;
    }
}