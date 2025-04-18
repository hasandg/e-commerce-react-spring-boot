package com.hasandag.ecommerce.payment.mapper;

import com.hasandag.ecommerce.payment.dto.PaymentRequest;
import com.hasandag.ecommerce.payment.dto.PaymentResponse;
import com.hasandag.ecommerce.payment.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "paymentIntentId", ignore = true)
    @Mapping(target = "errorMessage", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    Payment toEntity(PaymentRequest paymentRequest);
    
    PaymentResponse toDto(Payment payment);
} 