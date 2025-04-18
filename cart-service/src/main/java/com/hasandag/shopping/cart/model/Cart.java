package com.hasandag.ecommerce.cart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "carts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String userId;
    
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();
    
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    public void updateTotalAmount() {
        this.totalAmount = items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public void addItem(CartItem item) {
        // Check if product already exists in cart
        for (int i = 0; i < items.size(); i++) {
            CartItem existingItem = items.get(i);
            if (existingItem.getProductId().equals(item.getProductId())) {
                // If product exists, update quantity
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                existingItem.setUpdatedAt(LocalDateTime.now());
                items.set(i, existingItem);
                updateTotalAmount();
                return;
            }
        }
        
        // If product doesn't exist, add new item
        item.setCartId(this.id);
        items.add(item);
        updateTotalAmount();
    }
    
    public void removeItem(String productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
        updateTotalAmount();
    }
    
    public void updateItemQuantity(String productId, int quantity) {
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            if (item.getProductId().equals(productId)) {
                if (quantity <= 0) {
                    removeItem(productId);
                } else {
                    item.setQuantity(quantity);
                    item.setUpdatedAt(LocalDateTime.now());
                    items.set(i, item);
                }
                break;
            }
        }
        updateTotalAmount();
    }
    
    public void clear() {
        items.clear();
        totalAmount = BigDecimal.ZERO;
        updatedAt = LocalDateTime.now();
    }
} 