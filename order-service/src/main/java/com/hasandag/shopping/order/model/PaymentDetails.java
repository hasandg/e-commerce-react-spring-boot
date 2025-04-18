package com.hasandag.ecommerce.order.model;

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
public class PaymentDetails {
    
    private String paymentId;
    
    private PaymentMethod paymentMethod;
    
    private PaymentStatus paymentStatus;
    
    private BigDecimal amount;
    
    private String transactionId;
    
    private LocalDateTime paymentDate;
} 