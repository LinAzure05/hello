package com.agriculture.dto;

public class RegisterDTO {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String userType;
    private String realName;

    // 默认构造函数
    public RegisterDTO() {
    }

    // 带参数的构造函数
    public RegisterDTO(String username, String password, String email, String phone, String userType, String realName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
        this.realName = realName;
    }

    // Getter和Setter方法
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public String toString() {
        return "RegisterDTO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", userType='" + userType + '\'' +
                ", realName='" + realName + '\'' +
                '}';
    }
}