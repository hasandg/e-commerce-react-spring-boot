package com.hasandag.ecommerce.payment.dto;

import com.hasandag.ecommerce.payment.model.PaymentMethod;
import com.hasandag.ecommerce.payment.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    
    private String id;
    
    private String orderId;
    
    private String userId;
    
    private BigDecimal amount;
    
    private PaymentMethod paymentMethod;
    
    private PaymentStatus status;
    
    private String transactionId;
    
    private String errorMessage;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private LocalDateTime completedAt;
} 