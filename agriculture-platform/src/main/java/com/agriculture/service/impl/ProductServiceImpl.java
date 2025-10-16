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
    public boolean createProduct(Product product) {
        if (product == null || product.getMerchantId() == null) {
            return false;
        }
        if (product.getStatus() == null || product.getStatus().isBlank()) {
            product.setStatus("ACTIVE");
        }
        product.setStatus(product.getStatus().toUpperCase());
        if (product.getIsFeatured() == null) {
            product.setIsFeatured(Boolean.FALSE);
        }
        return productMapper.insert(product) > 0;
    }

    @Override
    public boolean updateProduct(Product product) {
        if (product == null || product.getProductId() == null || product.getMerchantId() == null) {
            return false;
        }
        if (product.getIsFeatured() == null) {
            product.setIsFeatured(Boolean.FALSE);
        }
        if (product.getStatus() != null) {
            product.setStatus(product.getStatus().toUpperCase());
        }
        return productMapper.updateProduct(product) > 0;
    }

    @Override
    public boolean updateProductStatus(Long productId, Long merchantId, String status) {
        if (productId == null || merchantId == null || status == null || status.isBlank()) {
            return false;
        }
        return productMapper.updateStatus(productId, merchantId, status.toUpperCase()) > 0;
    }

    @Override
    public boolean reduceStock(Long productId, int quantity) {
        if (productId == null || quantity <= 0) {
            return false;
        }
        return productMapper.decreaseStock(productId, quantity) > 0;
    }
}
