package com.example.spring.beanlifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class User implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean, DisposableBean {

    private Integer id;

    private String name;

    public User() {
        System.out.println("构造器方法User()");
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public void setBeanName(String s) {
        System.out.println("BeanNameAware的setBeanName()方法");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("BeanFactoryAware的()方法");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("ApplicationContextAware的setApplicationContext方法");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean的afterPropertiesSet()方法");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("DisposableBean的destroy()方法");
    }

    /**
     * 也可以通过@Bean(initMethod="testPostConstruct")来自定义初始化方法
     */
    @PostConstruct
    public void testPostConstruct() {
        System.out.println("自定义的@PostConstruct方法");
    }

    /**
     * 也可以通过@Bean(destroyMethod="testPreDestroy")来自定义销毁方法
     */
    @PreDestroy
    public void testPreDestroy() {
        System.out.println("自定义的testPreDestroy方法");
    }
}
