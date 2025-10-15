package com.agriculture.service.impl;

import com.agriculture.entity.Product;
import com.agriculture.mapper.ProductMapper;
import com.agriculture.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> getAllActiveProducts() {
        return productMapper.findAllActive();
    }

    @Override
    public List<Product> getFeaturedProducts() {
        return productMapper.findFeaturedProducts();
    }

    @Override
    public Product getProductById(Long productId) {
        return productMapper.findById(productId);
    }

    @Override
    public List<Product> getProductsByMerchant(Long merchantId) {
        return productMapper.findByMerchantId(merchantId);
    }

    @Override
    public boolean reduceStock(Long productId, int quantity) {
        if (productId == null || quantity <= 0) {
            return false;
        }
        return productMapper.decreaseStock(productId, quantity) > 0;
    }
}