package com.agriculture.service.impl;

import com.agriculture.entity.User;
import com.agriculture.mapper.UserMapper;
import com.agriculture.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public User findById(Long userId) {
        return userMapper.findById(userId);
    }

    @Override
    public boolean registerUser(User user) {
        // 检查用户名是否已存在
        if (findByUsername(user.getUsername()) != null) {
            return false;
        }

        // 检查邮箱是否已存在
        if (findByEmail(user.getEmail()) != null) {
            return false;
        }

        // 直接存储密码（不加密）
        user.setStatus("ACTIVE");
        user.setCreateTime(LocalDateTime.now());

        return userMapper.insert(user) > 0;
    }

    @Override
    public boolean updateUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.update(user) > 0;
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }
}