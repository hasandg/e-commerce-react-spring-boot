package com.hasandag.ecommerce.cart.service.impl;

import com.hasandag.ecommerce.cart.dto.CartItemRequest;
import com.hasandag.ecommerce.cart.dto.CartResponse;
import com.hasandag.ecommerce.cart.exception.CartNotFoundException;
import com.hasandag.ecommerce.cart.mapper.CartMapper;
import com.hasandag.ecommerce.cart.model.Cart;
import com.hasandag.ecommerce.cart.model.CartItem;
import com.hasandag.ecommerce.cart.repository.CartRepository;
import com.hasandag.ecommerce.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Override
    public CartResponse getCartByUserId(String userId) {
        Cart cart = getOrCreateCart(userId);
        return cartMapper.toCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addItemToCart(String userId, CartItemRequest cartItemRequest) {
        Cart cart = getOrCreateCart(userId);
        
        CartItem cartItem = cartMapper.toCartItem(cartItemRequest);
        cartItem.setCartId(cart.getId());
        
        cart.addItem(cartItem);
        cart.setUpdatedAt(LocalDateTime.now());
        
        Cart updatedCart = cartRepository.save(cart);
        log.info("Added item {} to cart for user {}", cartItemRequest.getProductId(), userId);
        
        return cartMapper.toCartResponse(updatedCart);
    }

    @Override
    @Transactional
    public CartResponse updateCartItemQuantity(String userId, String productId, int quantity) {
        Cart cart = getCartByUserIdOrThrow(userId);
        
        cart.updateItemQuantity(productId, quantity);
        cart.setUpdatedAt(LocalDateTime.now());
        
        Cart updatedCart = cartRepository.save(cart);
        log.info("Updated item {} quantity to {} in cart for user {}", productId, quantity, userId);
        
        return cartMapper.toCartResponse(updatedCart);
    }

    @Override
    @Transactional
    public CartResponse removeCartItem(String userId, String productId) {
        Cart cart = getCartByUserIdOrThrow(userId);
        
        cart.removeItem(productId);
        cart.setUpdatedAt(LocalDateTime.now());
        
        Cart updatedCart = cartRepository.save(cart);
        log.info("Removed item {} from cart for user {}", productId, userId);
        
        return cartMapper.toCartResponse(updatedCart);
    }

    @Override
    @Transactional
    public void clearCart(String userId) {
        Cart cart = getCartByUserIdOrThrow(userId);
        
        cart.clear();
        
        cartRepository.save(cart);
        log.info("Cleared cart for user {}", userId);
    }

    @Override
    @Transactional
    public void deleteCart(String userId) {
        if (cartRepository.existsByUserId(userId)) {
            cartRepository.deleteByUserId(userId);
            log.info("Deleted cart for user {}", userId);
        }
    }

    @Override
    public int getCartItemCount(String userId) {
        try {
            Cart cart = getCartByUserIdOrThrow(userId);
            return cart.getItems().size();
        } catch (CartNotFoundException e) {
            return 0;
        }
    }

    @Override
    public boolean checkProductInCart(String userId, String productId) {
        try {
            Cart cart = getCartByUserIdOrThrow(userId);
            return cart.getItems().stream()
                    .anyMatch(item -> item.getProductId().equals(productId));
        } catch (CartNotFoundException e) {
            return false;
        }
    }

    private Cart getOrCreateCart(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .userId(userId)
                            .build();
                    Cart savedCart = cartRepository.save(newCart);
                    log.info("Created new cart for user {}", userId);
                    return savedCart;
                });
    }

    private Cart getCartByUserIdOrThrow(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));
    }
} 