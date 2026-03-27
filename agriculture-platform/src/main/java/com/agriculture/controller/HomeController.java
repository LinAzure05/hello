package com.agriculture.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "助农服务平台 - 连接农民与消费者");
        model.addAttribute("welcomeMessage", "欢迎来到助农服务平台");
        return "index";  // 确保返回的是index.html
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}