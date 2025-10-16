package com.agriculture.controller;

import com.agriculture.dto.CartViewItem;
import com.agriculture.entity.CartItem;
import com.agriculture.entity.Product;
import com.agriculture.service.CartService;
import com.agriculture.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    // 临时用户ID（实际项目中应该从会话获取）
    private static final Long TEMP_CUSTOMER_ID = 3L; // 使用customer1的用户ID

    @GetMapping
    public String viewCart(Model model) {
        List<CartItem> cartItems = cartService.getCartItems(TEMP_CUSTOMER_ID);
        List<CartViewItem> viewItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = productService.getProductById(cartItem.getProductId());
            if (product == null) {
                cartService.removeFromCart(cartItem.getCartItemId());
                continue;
            }

            BigDecimal price = product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO;
            Integer stock = product.getStockQuantity();
            String unit = product.getUnit() != null ? product.getUnit() : "";
            String mainImage = product.getMainImage();
            String description = product.getDescription() != null ? product.getDescription() : "商品描述信息待完善";
            String productName = product.getProductName() != null ? product.getProductName() : "商品" + cartItem.getProductId();

            viewItems.add(new CartViewItem(
                    cartItem.getCartItemId(),
                    cartItem.getProductId(),
                    productName,
                    description,
                    price,
                    cartItem.getQuantity() != null ? cartItem.getQuantity() : 0,
                    stock,
                    unit,
                    mainImage
            ));
        }

        BigDecimal totalAmount = viewItems.stream()
                .map(CartViewItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalQuantity = viewItems.stream()
                .mapToInt(CartViewItem::getQuantity)
                .sum();

        model.addAttribute("cartItems", viewItems);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("totalAmount", totalAmount);
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

    @PostMapping("/clear")
    @ResponseBody
    public String clearCart() {
        boolean success = cartService.clearCart(TEMP_CUSTOMER_ID);
        return success ? "success" : "error";
    }

    @PostMapping("/checkout")
    @ResponseBody
    public String checkout() {
        List<CartItem> cartItems = cartService.getCartItems(TEMP_CUSTOMER_ID);
        if (cartItems.isEmpty()) {
            return "error: 购物车为空";
        }

        for (CartItem cartItem : cartItems) {
            Product product = productService.getProductById(cartItem.getProductId());
            if (product == null) {
                return "error: 商品信息不存在";
            }
            Integer stock = product.getStockQuantity();
            if (stock != null && cartItem.getQuantity() > stock) {
                return "error: 商品" + product.getProductName() + "库存不足";
            }
        }

        boolean success = cartService.checkout(TEMP_CUSTOMER_ID);
        return success ? "success" : "error: 结算失败，请稍后重试";
    }
}