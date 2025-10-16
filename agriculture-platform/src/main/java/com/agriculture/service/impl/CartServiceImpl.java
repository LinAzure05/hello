package com.agriculture.service.impl;

import com.agriculture.entity.CartItem;
import com.agriculture.entity.Product;
import com.agriculture.service.CartService;
import com.agriculture.service.ProductService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CartServiceImpl implements CartService {

    private final Map<Long, Map<Long, CartItem>> customerCarts = new ConcurrentHashMap<>();
    private final Map<Long, Long> cartIndex = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final ProductService productService;

    public CartServiceImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public boolean addToCart(Long customerId, Long productId, Integer quantity) {
        if (customerId == null || productId == null || quantity == null || quantity <= 0) {
            return false;
        }

        Map<Long, CartItem> cart = customerCarts.computeIfAbsent(customerId, key -> new ConcurrentHashMap<>());
        synchronized (cart) {
            Product product = productService.getProductById(productId);
            if (product == null) {
                return false;
            }

            int availableStock = normalizeStock(product.getStockQuantity());

            CartItem existingItem = cart.values().stream()
                    .filter(item -> productId.equals(item.getProductId()))
                    .findFirst()
                    .orElse(null);

            if (availableStock != Integer.MAX_VALUE && quantity > availableStock) {
                return false;
            }

            if (existingItem != null) {
                int newQuantity = existingItem.getQuantity() + quantity;
                if (availableStock != Integer.MAX_VALUE && newQuantity > availableStock) {
                    return false;
                }
                existingItem.setQuantity(newQuantity);
                existingItem.setUpdateTime(LocalDateTime.now());
            } else {
                CartItem newItem = new CartItem();
                long newId = idGenerator.getAndIncrement();
                LocalDateTime now = LocalDateTime.now();

                newItem.setCartItemId(newId);
                newItem.setCustomerId(customerId);
                newItem.setProductId(productId);
                newItem.setQuantity(quantity);
                newItem.setCreateTime(now);
                newItem.setUpdateTime(now);

                cart.put(newId, newItem);
                cartIndex.put(newId, customerId);
            }
        }
        return true;
    }

    @Override
    public List<CartItem> getCartItems(Long customerId) {
        Map<Long, CartItem> cart = customerCarts.get(customerId);
        if (cart == null || cart.isEmpty()) {
            return List.of();
        }
        return new ArrayList<>(cart.values());
    }

    @Override
    public boolean updateQuantity(Long cartItemId, Integer quantity) {
        if (cartItemId == null || quantity == null || quantity <= 0) {
            return false;
        }

        Long customerId = cartIndex.get(cartItemId);
        if (customerId == null) {
            return false;
        }

        Map<Long, CartItem> cart = customerCarts.get(customerId);
        if (cart == null) {
            return false;
        }

        CartItem item = cart.get(cartItemId);
        if (item == null) {
            return false;
        }

        synchronized (cart) {
            Product product = productService.getProductById(item.getProductId());
            if (product == null) {
                cart.remove(cartItemId);
                cartIndex.remove(cartItemId);
                return false;
            }

            int availableStock = normalizeStock(product.getStockQuantity());
            if (quantity > item.getQuantity()) {
                if (availableStock != Integer.MAX_VALUE && quantity > availableStock) {
                    return false;
                }
            }

            item.setQuantity(quantity);
            item.setUpdateTime(LocalDateTime.now());
        }
        return true;
    }

    @Override
    public boolean removeFromCart(Long cartItemId) {
        if (cartItemId == null) {
            return false;
        }

        Long customerId = cartIndex.remove(cartItemId);
        if (customerId != null) {
            Map<Long, CartItem> cart = customerCarts.get(customerId);
            if (cart == null) {
                return false;
            }
            synchronized (cart) {
                CartItem removed = cart.remove(cartItemId);
                return removed != null;
            }
        }

        for (Map.Entry<Long, Map<Long, CartItem>> entry : customerCarts.entrySet()) {
            Map<Long, CartItem> cart = entry.getValue();
            synchronized (cart) {
                if (cart.remove(cartItemId) != null) {
                    cartIndex.remove(cartItemId);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean clearCart(Long customerId) {
        if (customerId == null) {
            return false;
        }

        Map<Long, CartItem> cart = customerCarts.remove(customerId);
        if (cart == null || cart.isEmpty()) {
            return true;
        }

        cart.keySet().forEach(cartIndex::remove);
        return true;
    }

    @Override
    public boolean checkout(Long customerId) {
        if (customerId == null) {
            return false;
        }

        Map<Long, CartItem> cart = customerCarts.get(customerId);
        if (cart == null || cart.isEmpty()) {
            return false;
        }

        List<CartItem> items;
        synchronized (cart) {
            if (cart.isEmpty()) {
                return false;
            }
            items = new ArrayList<>(cart.values());
        }

        for (CartItem item : items) {
            Product product = productService.getProductById(item.getProductId());
            int availableStock = product == null ? 0 : normalizeStock(product.getStockQuantity());
            if (product == null || (availableStock != Integer.MAX_VALUE && item.getQuantity() > availableStock)) {
                return false;
            }
        }

        for (CartItem item : items) {
            boolean updated = productService.reduceStock(item.getProductId(), item.getQuantity());
            if (!updated) {
                return false;
            }
        }

        clearCart(customerId);
        return true;
    }

    private int normalizeStock(Integer stockQuantity) {
        if (stockQuantity == null) {
            return Integer.MAX_VALUE;
        }
        return Math.max(stockQuantity, 0);
    }
}
