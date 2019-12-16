package com.example.security.service.impl;


import com.example.security.entity.UserInfo;
import com.example.security.mapper.UserInfoMapper;
import com.example.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 获取用户列表
     *
     * @return
     */
    @Override
    public List<UserInfo> listUserInfo() {
        List<UserInfo> userInfos = userInfoMapper.listUserInfo();
        return userInfos;
    }

    /**
     * 插入用户
     *
     * @param userInfo
     * @return
     */
    @Override
    public int insertUserInfo(UserInfo userInfo) {
        int insert = userInfoMapper.insert(userInfo);
        return insert;
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */
    @Override
    public UserInfo getUserInfoByLoginName(String username) {
        UserInfo userInfo = userInfoMapper.getUserInfoByLoginName(username);
        return userInfo;
    }
}
