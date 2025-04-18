package com.hasandag.ecommerce.order.dto;

import com.hasandag.ecommerce.order.model.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @Valid
    @NotNull(message = "Shipping address is required")
    private AddressDto shippingAddress;
    
    @Valid
    private AddressDto billingAddress;
    
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
    
    private String notes;
} 