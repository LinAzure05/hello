package com.agriculture.mapper;

import com.agriculture.entity.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("SELECT * FROM products WHERE status = 'ACTIVE'")
    List<Product> findAllActive();

    @Select("SELECT * FROM products WHERE product_id = #{productId}")
    Product findById(Long productId);

    @Select("SELECT * FROM products WHERE merchant_id = #{merchantId}")
    List<Product> findByMerchantId(Long merchantId);

    @Select("SELECT * FROM products WHERE is_featured = 1 AND status = 'ACTIVE'")
    List<Product> findFeaturedProducts();

    @Update({
            "UPDATE products",
            "SET stock_quantity = stock_quantity - #{quantity}",
            "WHERE product_id = #{productId} AND stock_quantity >= #{quantity}"
    })
    int decreaseStock(@Param("productId") Long productId, @Param("quantity") int quantity);

    @Insert({
            "INSERT INTO products (product_name, description, merchant_id, price, stock_quantity, unit, main_image, tags, is_featured, status, create_time, update_time)",
            "VALUES (#{productName}, #{description}, #{merchantId}, #{price}, #{stockQuantity}, #{unit}, #{mainImage}, #{tags}, #{isFeatured}, #{status}, GETDATE(), GETDATE())"
    })
    @Options(useGeneratedKeys = true, keyProperty = "productId")
    int insert(Product product);

    @Update({
            "UPDATE products",
            "SET product_name = #{productName},",
            "    description = #{description},",
            "    price = #{price},",
            "    stock_quantity = #{stockQuantity},",
            "    unit = #{unit},",
            "    main_image = #{mainImage},",
            "    tags = #{tags},",
            "    is_featured = #{isFeatured},",
            "    status = #{status},",
            "    update_time = GETDATE()",
            "WHERE product_id = #{productId} AND merchant_id = #{merchantId}"
    })
    int updateProduct(Product product);

    @Update({
            "UPDATE products",
            "SET status = #{status},",
            "    update_time = GETDATE()",
            "WHERE product_id = #{productId} AND merchant_id = #{merchantId}"
    })
    int updateStatus(@Param("productId") Long productId,
                     @Param("merchantId") Long merchantId,
                     @Param("status") String status);
}
