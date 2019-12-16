package com.example.security.controller;

import com.example.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class HelloContoller {

    @Autowired
    private UserService userService;

    @GetMapping("say")
    public  String say() {
        //List<UserInfo> userInfos = userService.listUserInfo();
        //System.out.println("=====================================================");
        //System.out.println(userInfos);
        //System.out.println("=====================================================");
        return "卧槽, hello";
    }


}
