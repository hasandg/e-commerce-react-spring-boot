package com.hasandag.ecommerce.product.exception;

public class ReviewNotFoundException extends RuntimeException {
    
    public ReviewNotFoundException(String message) {
        super(message);
    }
    
    public ReviewNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static ReviewNotFoundException withId(String reviewId) {
        return new ReviewNotFoundException("Review not found with id: " + reviewId);
    }
} 