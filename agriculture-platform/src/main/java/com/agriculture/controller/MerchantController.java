package com.agriculture.controller;

import com.agriculture.entity.Product;
import com.agriculture.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/merchant")
public class MerchantController {

    private final ProductService productService;

    @Autowired
    public MerchantController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        Long merchantId = getMerchantId(session);
        if (merchantId == null) {
            return "redirect:/login";
        }
        model.addAttribute("pageTitle", "商家工作台");
        return "merchant/dashboard";
    }

    @GetMapping("/products")
    public String manageProducts(Model model, HttpSession session) {
        Long merchantId = getMerchantId(session);
        if (merchantId == null) {
            return "redirect:/login";
        }
        List<Product> products = productService.getProductsByMerchant(merchantId);
        model.addAttribute("products", products);
        return "merchant/product-list";
    }

    @GetMapping("/products/new")
    public String showCreateForm(Model model, HttpSession session) {
        Long merchantId = getMerchantId(session);
        if (merchantId == null) {
            return "redirect:/login";
        }
        Product product = new Product();
        product.setStatus("ACTIVE");
        product.setIsFeatured(Boolean.FALSE);
        model.addAttribute("formTitle", "新增农产品");
        model.addAttribute("product", product);
        model.addAttribute("isEdit", false);
        return "merchant/product-form";
    }

    @PostMapping("/products")
    public String createProduct(@ModelAttribute("product") Product product,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Long merchantId = getMerchantId(session);
        if (merchantId == null) {
            return "redirect:/login";
        }
        product.setMerchantId(merchantId);
        if (product.getStatus() == null || product.getStatus().isBlank()) {
            product.setStatus("ACTIVE");
        }
        if (product.getIsFeatured() == null) {
            product.setIsFeatured(Boolean.FALSE);
        }
        boolean created = productService.createProduct(product);
        if (created) {
            redirectAttributes.addFlashAttribute("successMessage", "农产品已成功创建。");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "农产品创建失败，请检查填写内容。");
        }
        return "redirect:/merchant/products";
    }

    @GetMapping("/products/{id}/edit")
    public String showEditForm(@PathVariable("id") Long productId,
                               Model model,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Long merchantId = getMerchantId(session);
        if (merchantId == null) {
            return "redirect:/login";
        }
        Product product = productService.getProductById(productId);
        if (product == null || product.getMerchantId() == null || !product.getMerchantId().equals(merchantId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "未找到指定的农产品或无权编辑。");
            return "redirect:/merchant/products";
        }
        model.addAttribute("formTitle", "编辑农产品");
        model.addAttribute("product", product);
        model.addAttribute("isEdit", true);
        return "merchant/product-form";
    }

    @PostMapping("/products/{id}")
    public String updateProduct(@PathVariable("id") Long productId,
                                @ModelAttribute("product") Product product,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Long merchantId = getMerchantId(session);
        if (merchantId == null) {
            return "redirect:/login";
        }
        product.setProductId(productId);
        product.setMerchantId(merchantId);
        if (product.getIsFeatured() == null) {
            product.setIsFeatured(Boolean.FALSE);
        }
        boolean updated = productService.updateProduct(product);
        if (updated) {
            redirectAttributes.addFlashAttribute("successMessage", "农产品信息已更新。");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "农产品更新失败，请稍后重试。");
        }
        return "redirect:/merchant/products";
    }

    @PostMapping("/products/{id}/deactivate")
    public String deactivateProduct(@PathVariable("id") Long productId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        Long merchantId = getMerchantId(session);
        if (merchantId == null) {
            return "redirect:/login";
        }
        boolean success = productService.updateProductStatus(productId, merchantId, "INACTIVE");
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "农产品已下架。");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "下架失败，请稍后重试。");
        }
        return "redirect:/merchant/products";
    }

    private Long getMerchantId(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object userType = session.getAttribute("currentUserType");
        if (userType == null || !"MERCHANT".equalsIgnoreCase(userType.toString())) {
            return null;
        }
        Object userId = session.getAttribute("currentUserId");
        if (userId instanceof Number number) {
            return number.longValue();
        }
        Object user = session.getAttribute("currentUser");
        if (user instanceof com.agriculture.entity.User currentUser && currentUser.getUserId() != null) {
            return currentUser.getUserId();
        }
        return null;
    }
}
