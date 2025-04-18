package com.hasandag.ecommerce.product.controller;

import com.hasandag.ecommerce.product.dto.ReviewRequest;
import com.hasandag.ecommerce.product.dto.ReviewResponse;
import com.hasandag.ecommerce.product.service.ReviewService;
import com.hasandag.ecommerce.shared.dto.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Review API", description = "Endpoints for managing product reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a new review", description = "Creates a new review for a product")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @Valid @RequestBody ReviewRequest reviewRequest,
            @AuthenticationPrincipal Jwt jwt) {
        
        String customerId = jwt.getSubject();
        ReviewResponse createdReview = reviewService.createReview(reviewRequest, customerId);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Review created successfully", createdReview));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update a review", description = "Updates an existing review")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable String id,
            @Valid @RequestBody ReviewRequest reviewRequest,
            @AuthenticationPrincipal Jwt jwt) {
        
        String customerId = jwt.getSubject();
        ReviewResponse updatedReview = reviewService.updateReview(id, reviewRequest, customerId);
        
        return ResponseEntity.ok(ApiResponse.success("Review updated successfully", updatedReview));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete a review", description = "Deletes an existing review")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt) {
        
        String userId = jwt.getSubject();
        String role = jwt.getClaimAsString("role");
        
        reviewService.deleteReview(id, userId, role);
        
        return ResponseEntity.ok(ApiResponse.success("Review deleted successfully", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a review by ID", description = "Retrieves a review by its ID")
    public ResponseEntity<ApiResponse<ReviewResponse>> getReviewById(@PathVariable String id) {
        ReviewResponse review = reviewService.getReviewById(id);
        return ResponseEntity.ok(ApiResponse.success(review));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get reviews by product", description = "Retrieves all reviews for a specific product")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getReviewsByProduct(
            @PathVariable String productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ReviewResponse> reviews = reviewService.getReviewsByProduct(productId, pageRequest);
        
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get reviews by user", description = "Retrieves all reviews created by a specific user")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getReviewsByUser(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ReviewResponse> reviews = reviewService.getReviewsByUser(userId, pageRequest);
        
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @GetMapping("/rating/{rating}")
    @Operation(summary = "Get reviews by rating", description = "Retrieves all reviews with a specific rating")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getReviewsByRating(
            @PathVariable int rating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ReviewResponse> reviews = reviewService.getReviewsByRating(rating, pageRequest);
        
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }
} 