package com.agriculture.service;

import com.agriculture.entity.CartItem;
import java.util.List;

public interface CartService {
    boolean addToCart(Long customerId, Long productId, Integer quantity);
    List<CartItem> getCartItems(Long customerId);
    boolean updateQuantity(Long cartItemId, Integer quantity);
    boolean removeFromCart(Long cartItemId);
    boolean clearCart(Long customerId);
    boolean checkout(Long customerId);
}