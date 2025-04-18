package com.hasandag.ecommerce.order.mapper;

import com.hasandag.ecommerce.order.dto.AddressDto;
import com.hasandag.ecommerce.order.dto.OrderItemDto;
import com.hasandag.ecommerce.order.dto.OrderRequest;
import com.hasandag.ecommerce.order.dto.OrderResponse;
import com.hasandag.ecommerce.order.model.Address;
import com.hasandag.ecommerce.order.model.Order;
import com.hasandag.ecommerce.order.model.OrderItem;
import com.hasandag.ecommerce.order.model.PaymentDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    
    Address toAddress(AddressDto addressDto);
    
    AddressDto toAddressDto(Address address);
    
    OrderItem toOrderItem(OrderItemDto orderItemDto);
    
    OrderItemDto toOrderItemDto(OrderItem orderItem);
    
    List<OrderItemDto> toOrderItemDtoList(List<OrderItem> items);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "taxAmount", ignore = true)
    @Mapping(target = "shippingAmount", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "trackingNumber", ignore = true)
    @Mapping(target = "paymentDetails", source = "paymentMethod", qualifiedByName = "toPaymentDetails")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "shippedAt", ignore = true)
    @Mapping(target = "deliveredAt", ignore = true)
    Order toOrder(OrderRequest orderRequest);
    
    @Named("toPaymentDetails")
    default PaymentDetails toPaymentDetails(com.hasandag.ecommerce.order.model.PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            return null;
        }
        
        return PaymentDetails.builder()
                .paymentMethod(paymentMethod)
                .paymentStatus(com.hasandag.ecommerce.order.model.PaymentStatus.PENDING)
                .build();
    }
    
    @Mapping(target = "paymentMethod", source = "paymentDetails.paymentMethod")
    @Mapping(target = "paymentStatus", source = "paymentDetails.paymentStatus")
    @Mapping(target = "transactionId", source = "paymentDetails.transactionId")
    @Mapping(target = "paymentDate", source = "paymentDetails.paymentDate")
    OrderResponse toOrderResponse(Order order);
} 