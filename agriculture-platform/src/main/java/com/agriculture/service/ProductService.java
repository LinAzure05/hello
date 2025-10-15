package com.agriculture.service;

import com.agriculture.entity.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAllActiveProducts();
    List<Product> getFeaturedProducts();
    Product getProductById(Long productId);
    List<Product> getProductsByMerchant(Long merchantId);
    boolean reduceStock(Long productId, int quantity);
}