package com.hasandag.ecommerce.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    
    private String id;
    
    private String productId;
    
    private String productName;
    
    private String productImageUrl;
    
    private int quantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal subtotal;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
} 