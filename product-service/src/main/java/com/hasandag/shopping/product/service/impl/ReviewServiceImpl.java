package com.hasandag.ecommerce.product.service.impl;

import com.hasandag.ecommerce.product.dto.ReviewRequest;
import com.hasandag.ecommerce.product.dto.ReviewResponse;
import com.hasandag.ecommerce.product.exception.ReviewNotFoundException;
import com.hasandag.ecommerce.product.exception.UnauthorizedAccessException;
import com.hasandag.ecommerce.product.mapper.ReviewMapper;
import com.hasandag.ecommerce.product.model.Review;
import com.hasandag.ecommerce.product.repository.ReviewRepository;
import com.hasandag.ecommerce.product.service.ProductService;
import com.hasandag.ecommerce.product.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ProductService productService;

    @Override
    @Transactional
    public ReviewResponse createReview(ReviewRequest reviewRequest, String userId) {
        Review review = reviewMapper.toEntity(reviewRequest);
        review.setUserId(userId);
        review.setApproved(false);  // Require approval before showing

        Review savedReview = reviewRepository.save(review);
        log.info("Review created with ID: {} for product: {}", savedReview.getId(), review.getProductId());
        
        // Update product rating
        updateProductRating(review.getProductId());
        
        return reviewMapper.toDto(savedReview);
    }

    @Override
    public ReviewResponse getReviewById(String id) {
        Review review = findReviewById(id);
        return reviewMapper.toDto(review);
    }

    @Override
    public Page<ReviewResponse> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable)
                .map(reviewMapper::toDto);
    }

    @Override
    public List<ReviewResponse> getReviewsByProductId(String productId) {
        List<Review> reviews = reviewRepository.findByProductIdAndApprovedTrue(productId);
        return reviews.stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponse> getReviewsByUserId(String userId) {
        // Create a pageable with large size to get all results
        Pageable allResults = PageRequest.of(0, Integer.MAX_VALUE);
        List<Review> reviews = reviewRepository.findByUserId(userId, allResults).getContent();
        return reviews.stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(String id, ReviewRequest reviewRequest, String userId) {
        Review existingReview = findReviewById(id);
        
        // Check if the user is authorized to update the review
        if (!existingReview.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("You are not authorized to update this review");
        }
        
        existingReview.setTitle(reviewRequest.getTitle());
        existingReview.setComment(reviewRequest.getComment());
        existingReview.setRating(reviewRequest.getRating());
        existingReview.setApproved(false);  // Require re-approval after update
        
        Review updatedReview = reviewRepository.save(existingReview);
        log.info("Review updated with ID: {}", updatedReview.getId());
        
        // Update product rating
        updateProductRating(updatedReview.getProductId());
        
        return reviewMapper.toDto(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(String id, String userId, String role) {
        Review review = findReviewById(id);
        
        // Check if the user is authorized to delete the review
        if (!review.getUserId().equals(userId) && !"ADMIN".equals(role)) {
            throw new UnauthorizedAccessException("You are not authorized to delete this review");
        }
        
        String productId = review.getProductId();
        reviewRepository.delete(review);
        log.info("Review deleted with ID: {}", id);
        
        // Update product rating
        updateProductRating(productId);
    }

    @Override
    @Transactional
    public ReviewResponse approveReview(String id) {
        Review review = findReviewById(id);
        review.setApproved(true);
        Review approvedReview = reviewRepository.save(review);
        log.info("Review approved with ID: {}", id);
        
        // Update product rating
        updateProductRating(review.getProductId());
        
        return reviewMapper.toDto(approvedReview);
    }

    @Override
    public double calculateAverageRating(String productId) {
        List<Review> reviews = reviewRepository.findByProductIdAndApprovedTrue(productId);
        
        if (reviews.isEmpty()) {
            return 0.0;
        }
        
        double totalRating = reviews.stream()
                .mapToInt(Review::getRating)
                .sum();
        
        return totalRating / reviews.size();
    }
    
    @Override
    public Page<ReviewResponse> getReviewsByProduct(String productId, PageRequest pageRequest) {
        Page<Review> reviews = reviewRepository.findByProductIdAndApprovedTrue(productId, pageRequest);
        return reviews.map(reviewMapper::toDto);
    }
    
    @Override
    public Page<ReviewResponse> getReviewsByUser(String userId, PageRequest pageRequest) {
        Page<Review> reviews = reviewRepository.findByUserId(userId, pageRequest);
        return reviews.map(reviewMapper::toDto);
    }
    
    @Override
    public Page<ReviewResponse> getReviewsByRating(int rating, PageRequest pageRequest) {
        Page<Review> reviews = reviewRepository.findByRating(rating, pageRequest);
        return reviews.map(reviewMapper::toDto);
    }
    
    private Review findReviewById(String id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + id));
    }
    
    private void updateProductRating(String productId) {
        double averageRating = calculateAverageRating(productId);
        int reviewCount = reviewRepository.findByProductIdAndApprovedTrue(productId).size();
        productService.updateProductRating(productId, averageRating, reviewCount);
        log.info("Product rating updated for product ID: {}, new average: {}, count: {}", 
                productId, averageRating, reviewCount);
    }
} 