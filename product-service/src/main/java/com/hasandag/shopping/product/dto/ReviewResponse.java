package com.hasandag.ecommerce.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private String id;
    private String productId;
    private String userId;
    private String title;
    private String comment;
    private int rating;
    private boolean approved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 