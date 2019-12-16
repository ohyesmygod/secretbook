package com.example.security.config.security;


import com.example.security.entity.SysPermission;
import com.example.security.entity.SysUser;
import com.example.security.mapper.SysPermissionMapper;
import com.example.security.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * 用来认证用户的
 * 可以在configure()方法中手动指定,即使不手动指定,只要该实例被装载进了ioc,就会自动生效,生效后控制台将不在打印默认的密码
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserMapper.getUserByUsername(username);
        if (null == sysUser) {
            throw new UsernameNotFoundException("用户不存在, 认证失败");
        }
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        Integer userId = sysUser.getId();
        List<SysPermission> sysPermissions = sysPermissionMapper.listPermissionByUserId(userId);
        System.out.println("登陆成功,该用户拥有的权限包括: " + sysPermissions);
        for (SysPermission permission : sysPermissions) {
            if (permission != null && permission.getName() != null) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getUrl());
                grantedAuthorityList.add(grantedAuthority);
            }
        }
        String password = sysUser.getPassword();
        return new User(username, password, grantedAuthorityList);
    }
}
