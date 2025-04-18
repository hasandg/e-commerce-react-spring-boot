package com.hasandag.ecommerce.product.exception;

public class CategoryNotFoundException extends RuntimeException {
    
    public CategoryNotFoundException(String message) {
        super(message);
    }
    
    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static CategoryNotFoundException withId(String categoryId) {
        return new CategoryNotFoundException("Category not found with id: " + categoryId);
    }
} 