package com.hasandag.ecommerce.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {
    
    @NotBlank(message = "Product ID is required")
    private String productId;
    
    @NotBlank(message = "Product name is required")
    private String productName;
    
    private String productImageUrl;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
    
    @Min(value = 0, message = "Unit price cannot be negative")
    private BigDecimal unitPrice;
} 