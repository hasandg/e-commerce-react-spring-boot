package com.hasandag.ecommerce.order.dto;

import com.hasandag.ecommerce.order.model.OrderStatus;
import com.hasandag.ecommerce.order.model.PaymentMethod;
import com.hasandag.ecommerce.order.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    
    private String id;
    
    private String userId;
    
    private String orderNumber;
    
    private List<OrderItemDto> items;
    
    private AddressDto shippingAddress;
    
    private AddressDto billingAddress;
    
    private PaymentMethod paymentMethod;
    
    private PaymentStatus paymentStatus;
    
    private String transactionId;
    
    private BigDecimal subtotal;
    
    private BigDecimal taxAmount;
    
    private BigDecimal shippingAmount;
    
    private BigDecimal totalAmount;
    
    private OrderStatus status;
    
    private String trackingNumber;
    
    private String notes;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private LocalDateTime shippedAt;
    
    private LocalDateTime deliveredAt;
    
    private LocalDateTime paymentDate;
} 