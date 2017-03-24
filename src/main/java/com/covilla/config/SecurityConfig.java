package com.covilla.config;

import com.covilla.common.AuthorityEnum;
import com.covilla.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.ExceptionTranslationFilter;

/**
 * Created by qmaolong on 2017/3/23.
 */
@EnableWebSecurity
public class SecurityConfig {
    private static String merchantLoginPage = "/merchant/login";
    private static String merchantMainPage = "/merchant/main";
    private static String sysLoginPage = "/sys/login";
    private static String sysMainPage = "sys/login";

    @Configuration
    @Order(1)
    public class MerchantSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth.userDetailsService(userDetailsService());
            auth.authenticationProvider(myAuthProvider());
//            super.configure(auth);
        }
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                    .antMatchers("/asset/**", "/merchant/secureLogin").permitAll()
                .antMatchers("/merchant/admin/**", "/merchant/main").hasRole(AuthorityEnum.MERCHANT.getName())
                    .and()
                    .csrf().disable()
//                    .addFilter(merchantExceptionTranslationFilter())
                    .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler()).authenticationEntryPoint(merchantAuthEntryPoint())
                    .and()
                    .headers().frameOptions().sameOrigin()
                    .and()
                .formLogin()
                    .loginPage("/merchant/login").permitAll()
                    .loginProcessingUrl("/merchant/secureLogin").permitAll()
                    .usernameParameter("name").passwordParameter("password")
                    .successHandler(merchantAuthSuccessHandler())
                    .failureHandler(merchantAuthFailHandler());
        }
    }

    /*@Configuration
    public static class SystemSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .and()
                    .formLogin();
        }
    }*/

    @Bean
    public MyAuthProvider myAuthProvider(){
        return new MyAuthProvider();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService()  {
        return new MyUserDetailsService();
    }
    @Bean
    public ExceptionTranslationFilter merchantExceptionTranslationFilter(){
        ExceptionTranslationFilter exceptionTranslationFilter = new ExceptionTranslationFilter(merchantAuthEntryPoint());
        exceptionTranslationFilter.setAccessDeniedHandler(myAccessDeniedHandler());
        return exceptionTranslationFilter;
    }

    @Bean
    public ExceptionTranslationFilter sysExceptionTranslationFilter(){
        ExceptionTranslationFilter exceptionTranslationFilter = new ExceptionTranslationFilter(sysAuthEntryPoint());
        exceptionTranslationFilter.setAccessDeniedHandler(myAccessDeniedHandler());
        return exceptionTranslationFilter;
    }

    @Bean AuthSuccessHandlerImpl merchantAuthSuccessHandler(){
        return new AuthSuccessHandlerImpl(merchantMainPage);
    }

    @Bean
    public AuthFailHandlerImpl merchantAuthFailHandler(){
        return new AuthFailHandlerImpl(merchantLoginPage);
    }

    @Bean AuthSuccessHandlerImpl sysAuthSuccessHandler(){
        return new AuthSuccessHandlerImpl(sysMainPage);
    }

    @Bean
    public AuthFailHandlerImpl sysAuthFailHandler(){
        return new AuthFailHandlerImpl(sysLoginPage);
    }

    @Bean
    public MyAccessDeniedHandlerImpl myAccessDeniedHandler(){
        return new MyAccessDeniedHandlerImpl();
    }

    @Bean
    public MyAuthEntryPoint merchantAuthEntryPoint(){
        return new MyAuthEntryPoint(merchantLoginPage);
    }

    @Bean
    public MyAuthEntryPoint sysAuthEntryPoint(){
        return new MyAuthEntryPoint(sysLoginPage);
    }

    /*@Bean
    public AuthSuccessHandlerImpl authSuccessHandler(String successUrl){
        return new AuthSuccessHandlerImpl(successUrl);
    }

    @Bean
    public AuthFailHandlerImpl authFailHandler(String loginUrl){
        return new AuthFailHandlerImpl(loginUrl);
    }*/


}
