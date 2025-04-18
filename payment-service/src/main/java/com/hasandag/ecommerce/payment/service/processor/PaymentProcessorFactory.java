package com.hasandag.ecommerce.payment.service.processor;

import com.hasandag.ecommerce.payment.exception.PaymentProcessingException;
import com.hasandag.ecommerce.payment.model.PaymentMethod;
import com.hasandag.ecommerce.payment.service.processor.impl.CreditCardPaymentProcessor;
import com.hasandag.ecommerce.payment.service.processor.impl.PayPalPaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentProcessorFactory {

    private final CreditCardPaymentProcessor creditCardPaymentProcessor;
    private final PayPalPaymentProcessor payPalPaymentProcessor;
    
    public PaymentProcessor getProcessor(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case CREDIT_CARD, DEBIT_CARD -> creditCardPaymentProcessor;
            case PAYPAL -> payPalPaymentProcessor;
            default -> throw new PaymentProcessingException("Unsupported payment method: " + paymentMethod);
        };
    }
} 