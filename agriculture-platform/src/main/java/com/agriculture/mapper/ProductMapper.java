package com.agriculture.mapper;

import com.agriculture.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("SELECT * FROM products WHERE status = 'ACTIVE'")
    List<Product> findAllActive();

    @Select("SELECT * FROM products WHERE product_id = #{productId}")
    Product findById(Long productId);

    @Select("SELECT * FROM products WHERE merchant_id = #{merchantId} AND status = 'ACTIVE'")
    List<Product> findByMerchantId(Long merchantId);

    @Select("SELECT * FROM products WHERE is_featured = 1 AND status = 'ACTIVE'")
    List<Product> findFeaturedProducts();
}