package com.hasandag.ecommerce.shared.dto;

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
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String categoryId;
    private String sellerId;
    private List<String> imageUrls;
    private double averageRating;
    private int reviewCount;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 