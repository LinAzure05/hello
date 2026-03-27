package com.agriculture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AgriculturePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgriculturePlatformApplication.class, args);
        System.out.println("=========================================");
        System.out.println("助农服务平台启动成功！");
        System.out.println("访问地址: http://localhost:8080");
        System.out.println("=========================================");
    }
}