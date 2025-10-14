package com.agriculture.controller;

import com.agriculture.entity.Product;
import com.agriculture.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public String productList(Model model) {
        List<Product> products = productService.getAllActiveProducts();
        model.addAttribute("products", products);
        model.addAttribute("title", "农产品商城");
        return "product/list";
    }

    @GetMapping("/detail/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            // 如果商品不存在，重定向到列表页
            return "redirect:/product/list";
        }
        model.addAttribute("product", product);
        return "product/detail";
    }
}