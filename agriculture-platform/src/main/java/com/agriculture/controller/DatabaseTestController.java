package com.agriculture.controller;

import com.agriculture.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseTestController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/test-db")
    public String testDatabase() {
        try {
            int count = userMapper.findAll().size();
            return "数据库连接成功！用户数量: " + count;
        } catch (Exception e) {
            return "数据库连接失败: " + e.getMessage();
        }
    }
}