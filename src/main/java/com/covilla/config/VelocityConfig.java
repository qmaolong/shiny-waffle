package com.covilla.config;

import com.covilla.util.MiscUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by qmaolong on 2017/3/24.
 */
@Configuration
public class VelocityConfig {
    @Bean
    public ViewResolver viewResolver(){
        VelocityViewResolver velocityViewResolver = new VelocityViewResolver();
        velocityViewResolver.setContentType("text/html;charset=UTF-8");
        velocityViewResolver.setPrefix("templates/");
        velocityViewResolver.setSuffix(".vm");
        velocityViewResolver.setExposeSpringMacroHelpers(true);
        velocityViewResolver.setExposeRequestAttributes(true);
        velocityViewResolver.setRequestContextAttribute("req");
        velocityViewResolver.setExposeSessionAttributes(true);
        velocityViewResolver.setAllowRequestOverride(true);
        velocityViewResolver.setAllowSessionOverride(true);
        return velocityViewResolver;
    }

    @Bean
    public VelocityConfigurer velocityConfigurer() throws Exception{
        VelocityConfigurer velocityConfigurer = new VelocityConfigurer();
        Properties properties = new Properties();
        properties.load(this.getClass().getResourceAsStream("/velocity.properties"));
        velocityConfigurer.setVelocityProperties(properties);
        return velocityConfigurer;
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter(){
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(new MediaType("text", "html", MiscUtils.toMap("charset", "utf-8")));
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        return mappingJackson2HttpMessageConverter;
    }
}
