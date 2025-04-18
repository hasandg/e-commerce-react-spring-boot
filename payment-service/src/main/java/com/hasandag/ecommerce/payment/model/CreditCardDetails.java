package com.hasandag.ecommerce.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardDetails {
    
    private String cardNumber;
    
    private String cardholderName;
    
    private String expiryMonth;
    
    private String expiryYear;
    
    private String cvv;
} 