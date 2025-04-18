package com.hasandag.ecommerce.cart.controller;

import com.hasandag.ecommerce.cart.dto.CartItemRequest;
import com.hasandag.ecommerce.cart.dto.CartResponse;
import com.hasandag.ecommerce.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable String userId) {
        log.info("Fetching cart for user: {}", userId);
        CartResponse cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartResponse> addItemToCart(
            @PathVariable String userId,
            @Valid @RequestBody CartItemRequest cartItemRequest) {
        log.info("Adding item to cart for user: {}", userId);
        CartResponse updatedCart = cartService.addItemToCart(userId, cartItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCart);
    }

    @PutMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartResponse> updateCartItemQuantity(
            @PathVariable String userId,
            @PathVariable String productId,
            @RequestParam int quantity) {
        log.info("Updating quantity for item {} in cart for user: {}", productId, userId);
        CartResponse updatedCart = cartService.updateCartItemQuantity(userId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartResponse> removeCartItem(
            @PathVariable String userId,
            @PathVariable String productId) {
        log.info("Removing item {} from cart for user: {}", productId, userId);
        CartResponse updatedCart = cartService.removeCartItem(userId, productId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable String userId) {
        log.info("Clearing cart for user: {}", userId);
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteCart(@PathVariable String userId) {
        log.info("Deleting cart for user: {}", userId);
        cartService.deleteCart(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/count")
    public ResponseEntity<Integer> getCartItemCount(@PathVariable String userId) {
        log.info("Getting item count in cart for user: {}", userId);
        int count = cartService.getCartItemCount(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{userId}/check/{productId}")
    public ResponseEntity<Boolean> checkProductInCart(
            @PathVariable String userId,
            @PathVariable String productId) {
        log.info("Checking if product {} exists in cart for user: {}", productId, userId);
        boolean exists = cartService.checkProductInCart(userId, productId);
        return ResponseEntity.ok(exists);
    }
} 