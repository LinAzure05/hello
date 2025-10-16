package com.agriculture.controller;

import com.agriculture.dto.LoginDTO;
import com.agriculture.dto.RegisterDTO;
import com.agriculture.entity.User;
import com.agriculture.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegisterDTO registerDTO, Model model) {
        try {
            User user = new User();
            user.setUsername(registerDTO.getUsername());
            user.setPassword(registerDTO.getPassword());
            user.setEmail(registerDTO.getEmail());
            user.setPhone(registerDTO.getPhone());
            user.setUserType(registerDTO.getUserType());
            user.setRealName(registerDTO.getRealName());
            user.setAddress("待填写");

            boolean success = userService.registerUser(user);
            if (success) {
                model.addAttribute("successMessage", "注册成功！请登录。");
                return "login";
            } else {
                model.addAttribute("errorMessage", "用户名或邮箱已存在！");
                return "register";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "注册失败：" + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute LoginDTO loginDTO, Model model) {
        User user = userService.findByUsername(loginDTO.getUsername());
        if (user == null || !user.getPassword().equals(loginDTO.getPassword())) {
            model.addAttribute("errorMessage", "用户名或密码错误！");
            return "login";
        }

        String userType = user.getUserType() != null ? user.getUserType().toUpperCase() : "";
        switch (userType) {
            case "CUSTOMER":
                return "redirect:/product/list";
            case "MERCHANT":
                return "redirect:/merchant/dashboard";
            default:
                return "redirect:/dashboard";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard() {
        // 暂时重定向到商品列表页
        return "redirect:/product/list";
    }
}