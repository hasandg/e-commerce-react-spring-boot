package com.hasandag.ecommerce.payment.service.processor.impl;

import com.hasandag.ecommerce.payment.exception.PaymentProcessingException;
import com.hasandag.ecommerce.payment.model.CreditCardDetails;
import com.hasandag.ecommerce.payment.model.Payment;
import com.hasandag.ecommerce.payment.model.PaymentMethod;
import com.hasandag.ecommerce.payment.service.processor.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class CreditCardPaymentProcessor implements PaymentProcessor {

    @Override
    public String processPayment(Payment payment) {
        try {
            // In a real application, this would communicate with a payment gateway
            // Here we just simulate a successful payment process
            
            // Validate card details if needed
            validateCardDetails(payment);
            
            // Generate a transaction ID
            String transactionId = "CC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            
            // Simulate processing time
            simulateProcessingDelay();
            
            log.info("Successfully processed credit card payment for order: {}", payment.getOrderId());
            
            return transactionId;
        } catch (Exception e) {
            log.error("Error processing credit card payment", e);
            throw new PaymentProcessingException("Credit card payment processing failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void refundPayment(Payment payment) {
        try {
            // In a real application, this would communicate with a payment gateway
            // Here we just simulate a successful refund process
            
            // Validate the refund
            if (payment.getTransactionId() == null || payment.getTransactionId().isEmpty()) {
                throw new PaymentProcessingException("Cannot refund payment without transaction ID");
            }
            
            // Simulate processing time
            simulateProcessingDelay();
            
            log.info("Successfully refunded credit card payment for order: {}", payment.getOrderId());
        } catch (Exception e) {
            log.error("Error refunding credit card payment", e);
            throw new PaymentProcessingException("Credit card refund failed: " + e.getMessage(), e);
        }
    }
    
    private void validateCardDetails(Payment payment) {
        // This is a simplified validation
        // In a real application, more thorough validation would be needed
        
        if (payment.getPaymentMethod() != PaymentMethod.CREDIT_CARD) {
            throw new PaymentProcessingException("Invalid payment method: " + payment.getPaymentMethod());
        }
        
        CreditCardDetails cardDetails = payment.getCardDetails();
        
        if (cardDetails == null) {
            throw new PaymentProcessingException("Credit card details are required");
        }
        
        if (cardDetails.getCardNumber() == null || cardDetails.getCardNumber().length() < 13) {
            throw new PaymentProcessingException("Invalid card number");
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