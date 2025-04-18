package com.hasandag.ecommerce.shared.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private String id;
    private String cartId;
    private String productId;
    private String productName;
    private String productImageUrl;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
} 