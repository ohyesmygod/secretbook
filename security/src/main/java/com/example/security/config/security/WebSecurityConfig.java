package com.example.security.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 用来配置用户认证相关的东西,主要是指定userDetails相关的东西,目前也可以理解为登陆相关的东西
     * 其实不指定userDetails也行,只要ioc中存在userDetails实例,会自动生效,并且控制台不在打印默认的密码
     *
     * @param auth 认证管理器构建器
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);


        //自定义的userDetailsService和 auth.jdbcAuthentication()/auth.inMemoryAuthentication()是平级
        // auth.jdbcAuthentication()
        //JdbcUserDetailsManager
        //auth.inMemoryAuthentication();
        //InMemoryUserDetailsManager
    }

    /**
     * 用来配置授权相关的东西,也就是开发者可以通过覆盖这个方法来指定用户或者角色与对应url的访问权限
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //authorizeRequests()方法代表开启授权配置
        //authenticated()方法代表认证后才能访问
        http.formLogin()
                .and()
                .authorizeRequests()
                //.antMatchers("/test/admin/list", "/test/admin/list").access("@rbacService.hasPermission(request, authentication)")
                .antMatchers("/test/**").access("@rbacService.hasPermission(request, authentication)")
                .anyRequest().authenticated();
    }


}
