package com.hasandag.ecommerce.payment.service.processor;

import com.hasandag.ecommerce.payment.model.Payment;

public interface PaymentProcessor {
    
    String processPayment(Payment payment);
    
    void refundPayment(Payment payment);
} 