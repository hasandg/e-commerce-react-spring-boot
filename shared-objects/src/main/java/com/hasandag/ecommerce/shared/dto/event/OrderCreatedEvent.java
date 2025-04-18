package com.hasandag.ecommerce.shared.dto.event;

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
public class OrderCreatedEvent {
    private String orderId;
    private String userId;
    private List<OrderItemEvent> items;
    private BigDecimal totalAmount;
    private String paymentId;
    private String shippingAddressId;
    private LocalDateTime createdAt;
} 