package com.agriculture.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/test")
    public String testPage() {
        return "test";  // 返回test.html
    }

    @GetMapping("/test2")
    public String test2Page() {
        return "test2"; // 返回一个不存在的页面，测试错误
    }
}