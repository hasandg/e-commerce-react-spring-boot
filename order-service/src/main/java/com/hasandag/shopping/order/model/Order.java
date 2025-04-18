package com.hasandag.ecommerce.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    private String orderNumber;
    
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
    
    private Address shippingAddress;
    
    private Address billingAddress;
    
    private PaymentDetails paymentDetails;
    
    private BigDecimal subtotal;
    
    private BigDecimal taxAmount;
    
    private BigDecimal shippingAmount;
    
    private BigDecimal totalAmount;
    
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;
    
    private String trackingNumber;
    
    private String notes;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    private LocalDateTime shippedAt;
    
    private LocalDateTime deliveredAt;
    
    public void calculateTotals() {
        this.subtotal = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate tax at 10% of subtotal
        this.taxAmount = subtotal.multiply(new BigDecimal("0.10"));
        
        // Set shipping amount based on order value or other logic
        if (subtotal.compareTo(new BigDecimal("100")) > 0) {
            this.shippingAmount = BigDecimal.ZERO; // Free shipping for orders over $100
        } else {
            this.shippingAmount = new BigDecimal("10.00"); // $10 shipping fee
        }
        
        // Calculate total
        this.totalAmount = subtotal.add(taxAmount).add(shippingAmount);
    }
} 