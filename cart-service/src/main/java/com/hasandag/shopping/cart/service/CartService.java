package com.hasandag.ecommerce.cart.service;

import com.hasandag.ecommerce.cart.dto.CartItemRequest;
import com.hasandag.ecommerce.cart.dto.CartResponse;

public interface CartService {
    
    CartResponse getCartByUserId(String userId);
    
    CartResponse addItemToCart(String userId, CartItemRequest cartItemRequest);
    
    CartResponse updateCartItemQuantity(String userId, String productId, int quantity);
    
    CartResponse removeCartItem(String userId, String productId);
    
    void clearCart(String userId);
    
    void deleteCart(String userId);
    
    int getCartItemCount(String userId);
    
    boolean checkProductInCart(String userId, String productId);
} 