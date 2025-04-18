package com.hasandag.ecommerce.payment.model;

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

@Document(collection = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    
    @Id
    private String id;
    
    @Indexed
    private String orderId;
    
    @Indexed
    private String userId;
    
    private BigDecimal amount;
    
    private PaymentMethod paymentMethod;
    
    private CreditCardDetails cardDetails;
    
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;
    
    private String transactionId;
    
    private String paymentIntentId;
    
    private String errorMessage;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    private LocalDateTime completedAt;
} 