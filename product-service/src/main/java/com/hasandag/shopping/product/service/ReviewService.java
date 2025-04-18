package com.hasandag.ecommerce.product.service;

import com.hasandag.ecommerce.product.dto.ReviewRequest;
import com.hasandag.ecommerce.product.dto.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    
    ReviewResponse createReview(ReviewRequest reviewRequest, String userId);
    
    ReviewResponse getReviewById(String id);
    
    Page<ReviewResponse> getAllReviews(Pageable pageable);
    
    List<ReviewResponse> getReviewsByProductId(String productId);
    
    List<ReviewResponse> getReviewsByUserId(String userId);
    
    ReviewResponse updateReview(String id, ReviewRequest reviewRequest, String userId);
    
    void deleteReview(String id, String userId, String role);
    
    ReviewResponse approveReview(String id);
    
    double calculateAverageRating(String productId);
    
    Page<ReviewResponse> getReviewsByProduct(String productId, PageRequest pageRequest);
    
    Page<ReviewResponse> getReviewsByUser(String userId, PageRequest pageRequest);
    
    Page<ReviewResponse> getReviewsByRating(int rating, PageRequest pageRequest);
} 