package com.hasandag.ecommerce.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    
    private String id;
    
    private String userId;
    
    private List<CartItemResponse> items;
    
    private BigDecimal totalAmount;
    
    private int itemCount;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
} 