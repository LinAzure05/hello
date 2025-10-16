package com.agriculture.dto;

import java.math.BigDecimal;

public class CartViewItem {
    private final Long cartItemId;
    private final Long productId;
    private final String productName;
    private final String description;
    private final BigDecimal price;
    private final Integer quantity;
    private final Integer stockQuantity;
    private final String unit;
    private final String mainImage;
    private final BigDecimal subtotal;

    public CartViewItem(Long cartItemId, Long productId, String productName, String description,
                        BigDecimal price, Integer quantity, Integer stockQuantity, String unit, String mainImage) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price != null ? price : BigDecimal.ZERO;
        this.quantity = quantity != null ? quantity : 0;
        this.stockQuantity = stockQuantity;
        this.unit = unit != null ? unit : "";
        this.mainImage = (mainImage != null && !mainImage.isBlank()) ? mainImage : null;
        this.subtotal = this.price.multiply(BigDecimal.valueOf(this.quantity.longValue()));
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getMainImage() {
        return mainImage;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
