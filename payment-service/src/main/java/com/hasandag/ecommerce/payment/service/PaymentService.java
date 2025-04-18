package com.hasandag.ecommerce.payment.service;

import com.hasandag.ecommerce.payment.dto.PaymentRequest;
import com.hasandag.ecommerce.payment.dto.PaymentResponse;
import com.hasandag.ecommerce.payment.model.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentService {
    
    PaymentResponse createPayment(PaymentRequest paymentRequest);
    
    PaymentResponse processPayment(String paymentId);
    
    PaymentResponse getPaymentById(String id);
    
    PaymentResponse getPaymentByOrderId(String orderId);
    
    Page<PaymentResponse> getPaymentsByUserId(String userId, Pageable pageable);
    
    List<PaymentResponse> getPaymentsByStatus(PaymentStatus status);
    
    List<PaymentResponse> getPaymentsByUserIdAndStatus(String userId, PaymentStatus status);
    
    List<PaymentResponse> getPaymentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    
    PaymentResponse updatePaymentStatus(String id, PaymentStatus status);
    
    PaymentResponse refundPayment(String id);
    
    void deletePayment(String id);
} 