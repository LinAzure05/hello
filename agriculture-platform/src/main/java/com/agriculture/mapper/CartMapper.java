package com.agriculture.mapper;

import com.agriculture.entity.CartItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {

    @Select("SELECT * FROM cart_items WHERE customer_id = #{customerId}")
    List<CartItem> findByCustomerId(Long customerId);

    @Select("SELECT * FROM cart_items WHERE customer_id = #{customerId} AND product_id = #{productId}")
    CartItem findByCustomerIdAndProductId(@Param("customerId") Long customerId, @Param("productId") Long productId);

    @Insert("INSERT INTO cart_items (customer_id, product_id, quantity) VALUES (#{customerId}, #{productId}, #{quantity})")
    int insert(CartItem cartItem);

    @Update("UPDATE cart_items SET quantity = #{quantity}, update_time = GETDATE() WHERE cart_item_id = #{cartItemId}")
    int updateQuantity(CartItem cartItem);

    @Delete("DELETE FROM cart_items WHERE cart_item_id = #{cartItemId}")
    int deleteById(Long cartItemId);

    @Delete("DELETE FROM cart_items WHERE customer_id = #{customerId}")
    int clearCart(Long customerId);
}