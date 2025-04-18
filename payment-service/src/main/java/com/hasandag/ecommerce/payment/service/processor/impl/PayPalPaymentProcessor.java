package com.hasandag.ecommerce.payment.service.processor.impl;

import com.hasandag.ecommerce.payment.exception.PaymentProcessingException;
import com.hasandag.ecommerce.payment.model.Payment;
import com.hasandag.ecommerce.payment.service.processor.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class PayPalPaymentProcessor implements PaymentProcessor {

    @Override
    public String processPayment(Payment payment) {
        try {
            // In a real application, this would communicate with PayPal API
            // Here we just simulate a successful payment process
            
            // Generate a transaction ID
            String transactionId = "PP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            
            // Simulate processing time
            simulateProcessingDelay();
            
            log.info("Successfully processed PayPal payment for order: {}", payment.getOrderId());
            
            return transactionId;
        } catch (Exception e) {
            log.error("Error processing PayPal payment", e);
            throw new PaymentProcessingException("PayPal payment processing failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void refundPayment(Payment payment) {
        try {
            // In a real application, this would communicate with PayPal API
            // Here we just simulate a successful refund process
            
            // Validate the refund
            if (payment.getTransactionId() == null || payment.getTransactionId().isEmpty()) {
                throw new PaymentProcessingException("Cannot refund payment without transaction ID");
            }
            
            // Simulate processing time
            simulateProcessingDelay();
            
            log.info("Successfully refunded PayPal payment for order: {}", payment.getOrderId());
        } catch (Exception e) {
            log.error("Error refunding PayPal payment", e);
            throw new PaymentProcessingException("PayPal refund failed: " + e.getMessage(), e);
        }
    }
    
    private void simulateProcessingDelay() {
        try {
            // Simulate a slight delay in processing
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
} 