package com.agriculture.mapper;

import com.agriculture.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);

    @Select("SELECT * FROM users WHERE user_id = #{userId}")
    User findById(Long userId);

    @Insert("INSERT INTO users (username, password, email, phone, user_type, real_name, address, business_license, status, create_time) " +
            "VALUES (#{username}, #{password}, #{email}, #{phone}, #{userType}, #{realName}, #{address}, #{businessLicense}, #{status}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);

    @Update("UPDATE users SET email=#{email}, phone=#{phone}, real_name=#{realName}, address=#{address}, update_time=#{updateTime} WHERE user_id=#{userId}")
    int update(User user);

    @Select("SELECT * FROM users")
    List<User> findAll();
}