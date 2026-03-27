package com.agriculture.service.impl;

import com.agriculture.entity.CartItem;
import com.agriculture.mapper.CartMapper;
import com.agriculture.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Override
    public boolean addToCart(Long customerId, Long productId, Integer quantity) {
        try {
            System.out.println("=== 开始添加购物车 ===");
            System.out.println("用户ID: " + customerId);
            System.out.println("商品ID: " + productId);
            System.out.println("数量: " + quantity);

            // 检查是否已存在购物车中
            System.out.println("检查是否已存在购物车中...");
            CartItem existingItem = cartMapper.findByCustomerIdAndProductId(customerId, productId);
            System.out.println("已存在商品: " + existingItem);

            if (existingItem != null) {
                // 更新数量
                System.out.println("更新已有商品数量...");
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                int result = cartMapper.updateQuantity(existingItem);
                System.out.println("更新结果: " + result);
                return result > 0;
            } else {
                // 新增
                System.out.println("添加新商品到购物车...");
                CartItem newItem = new CartItem();
                newItem.setCustomerId(customerId);
                newItem.setProductId(productId);
                newItem.setQuantity(quantity);
                int result = cartMapper.insert(newItem);
                System.out.println("新增结果: " + result);
                System.out.println("影响行数: " + result);
                return result > 0;
            }
        } catch (Exception e) {
            System.out.println("!!! 添加购物车异常 !!!");
            System.out.println("异常信息: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<CartItem> getCartItems(Long customerId) {
        try {
            System.out.println("获取购物车商品，用户ID: " + customerId);
            List<CartItem> items = cartMapper.findByCustomerId(customerId);
            System.out.println("找到 " + items.size() + " 个商品");
            return items;
        } catch (Exception e) {
            System.out.println("获取购物车异常: " + e.getMessage());
            return List.of(); // 返回空列表
        }
    }

    @Override
    public boolean updateQuantity(Long cartItemId, Integer quantity) {
        try {
            System.out.println("更新购物车商品数量");
            System.out.println("购物车项ID: " + cartItemId);
            System.out.println("新数量: " + quantity);

            CartItem cartItem = new CartItem();
            cartItem.setCartItemId(cartItemId);
            cartItem.setQuantity(quantity);
            int result = cartMapper.updateQuantity(cartItem);
            System.out.println("更新结果: " + result);
            return result > 0;
        } catch (Exception e) {
            System.out.println("更新数量异常: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean removeFromCart(Long cartItemId) {
        try {
            System.out.println("删除购物车商品");
            System.out.println("购物车项ID: " + cartItemId);

            int result = cartMapper.deleteById(cartItemId);
            System.out.println("删除结果: " + result);
            return result > 0;
        } catch (Exception e) {
            System.out.println("删除异常: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean clearCart(Long customerId) {
        try {
            System.out.println("清空购物车，用户ID: " + customerId);
            int result = cartMapper.clearCart(customerId);
            System.out.println("清空结果: " + result);
            return result > 0;
        } catch (Exception e) {
            System.out.println("清空异常: " + e.getMessage());
            return false;
        }
    }
}