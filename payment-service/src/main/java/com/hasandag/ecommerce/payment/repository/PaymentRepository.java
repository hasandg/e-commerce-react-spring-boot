package com.hasandag.ecommerce.payment.repository;

import com.hasandag.ecommerce.payment.model.Payment;
import com.hasandag.ecommerce.payment.model.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    
    Optional<Payment> findByOrderId(String orderId);
    
    Page<Payment> findByUserId(String userId, Pageable pageable);
    
    List<Payment> findByStatus(PaymentStatus status);
    
    List<Payment> findByUserIdAndStatus(String userId, PaymentStatus status);
    
    List<Payment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    Optional<Payment> findByTransactionId(String transactionId);
    
    Optional<Payment> findByPaymentIntentId(String paymentIntentId);
} 