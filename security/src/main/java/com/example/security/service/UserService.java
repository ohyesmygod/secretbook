package com.example.security.service;




import com.example.security.entity.UserInfo;

import java.util.List;

public interface UserService {

    /**
     * 获取用户列表
     * @return
     */
    List<UserInfo> listUserInfo();

    /**
     * 插入用户
     * @param userInfo
     * @return
     */
    int insertUserInfo(UserInfo userInfo);

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    UserInfo getUserInfoByLoginName(String username);
}
