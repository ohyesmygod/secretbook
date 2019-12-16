package com.example.security.service.impl;


import com.example.security.entity.SysPermission;
import com.example.security.entity.SysUser;
import com.example.security.mapper.SysPermissionMapper;
import com.example.security.mapper.SysUserMapper;
import com.example.security.service.RbacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//从这里实现动态的rbac
@Component("rbacService")
public class RbacServiceImpl implements RbacService {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    /**
     * 框架判断权限需要三个条件
     * 1.当前资源,可以从request中获得
     * 2.访问当前资源需要的权限,配置在数据库中
     * 3.当前用户拥有的权限,可以从authentication获得
     * 在这个自定义的方法中只需要判断用户拥有的权限对应的url是否包含request中的url即可
     *
     * @param request
     * @param authentication
     * @return
     */
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        //当事人
        //只有对非匿名用户才有必要进行权限控制
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            //username是用户名
            String username = ((UserDetails) principal).getUsername();
            //查询用户所拥有的权限对应的url
            SysUser user = sysUserMapper.getUserByUsername(username);
            return confirmUserPermission(user, request);
        }
        return false;
    }


    /**
     * 判断用户是否拥有该权限
     *
     * @param user
     * @param request
     * @return
     */
    public boolean confirmUserPermission(SysUser user, HttpServletRequest request) {
        if (user != null) {
            Integer userId = user.getId();
            List<SysPermission> permissions = sysPermissionMapper.listPermissionByUserId(userId);
            String requestUri = request.getRequestURI();
            System.out.println("===========拥有的权限: " + permissions);
            System.out.println("===========访问的资源: " + requestUri);
            for (SysPermission permission : permissions) {
                if (antPathMatcher.match(permission.getUrl(), requestUri)) {
                    return true;
                }
            }
        }
        return false;
    }
}
