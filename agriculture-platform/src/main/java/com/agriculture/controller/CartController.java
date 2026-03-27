package com.agriculture.controller;

import com.agriculture.entity.CartItem;
import com.agriculture.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // 临时用户ID（实际项目中应该从会话获取）
    private static final Long TEMP_CUSTOMER_ID = 3L; // 使用customer1的用户ID

    @GetMapping
    public String viewCart(Model model) {
        List<CartItem> cartItems = cartService.getCartItems(TEMP_CUSTOMER_ID);
        model.addAttribute("cartItems", cartItems);
        return "cart/view";
    }

    @PostMapping("/add")
    @ResponseBody
    public String addToCart(@RequestParam Long productId, @RequestParam Integer quantity) {
        boolean success = cartService.addToCart(TEMP_CUSTOMER_ID, productId, quantity);
        return success ? "success" : "error";
    }

    @PostMapping("/update")
    @ResponseBody
    public String updateQuantity(@RequestParam Long cartItemId, @RequestParam Integer quantity) {
        boolean success = cartService.updateQuantity(cartItemId, quantity);
        return success ? "success" : "error";
    }

    @PostMapping("/remove")
    @ResponseBody
    public String removeFromCart(@RequestParam Long cartItemId) {
        boolean success = cartService.removeFromCart(cartItemId);
        return success ? "success" : "error";
    }
}