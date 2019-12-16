package com.example.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestContoller {


    @GetMapping("admin/list")
    public  String adminList() {

        return "[admin, admin, admin, admin, admin]";
    }

    @GetMapping("user/list")
    public  String userList() {

        return "[user, user, user, user, user]";
    }



}
