package com.hasandag.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    
    private String id;
    
    private String productId;
    
    private String productName;
    
    private String productImageUrl;
    
    private int quantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal subtotal;
} 