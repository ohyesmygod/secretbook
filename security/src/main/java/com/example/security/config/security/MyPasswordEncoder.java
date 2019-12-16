package com.example.security.config.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 自定义的加密器,必须是PasswordEncoder的子类,当然也可以不采用这种方式,而是直接通过@Bean标签装配到ioc容器当中,security本身已经提供了很多默认的实现
 */
@Component
public class MyPasswordEncoder extends BCryptPasswordEncoder {
}
