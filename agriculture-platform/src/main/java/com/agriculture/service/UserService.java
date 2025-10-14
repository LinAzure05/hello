package com.agriculture.service;

import com.agriculture.entity.User;
import java.util.List;

public interface UserService {
    User findByUsername(String username);
    User findByEmail(String email);
    User findById(Long userId);
    boolean registerUser(User user);
    boolean updateUser(User user);
    List<User> getAllUsers();
}