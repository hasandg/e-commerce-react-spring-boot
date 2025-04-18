package com.hasandag.ecommerce.order.service;

import com.hasandag.ecommerce.order.dto.OrderRequest;
import com.hasandag.ecommerce.order.dto.OrderResponse;
import com.hasandag.ecommerce.order.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    
    OrderResponse createOrder(OrderRequest orderRequest, List<String> cartItemIds);
    
    OrderResponse getOrderById(String id);
    
    OrderResponse getOrderByIdAndUserId(String id, String userId);
    
    OrderResponse getOrderByOrderNumber(String orderNumber);
    
    Page<OrderResponse> getOrdersByUserId(String userId, Pageable pageable);
    
    List<OrderResponse> getOrdersByStatus(OrderStatus status);
    
    List<OrderResponse> getOrdersByUserIdAndStatus(String userId, OrderStatus status);
    
    List<OrderResponse> getOrdersBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    
    OrderResponse updateOrderStatus(String id, OrderStatus status);
    
    OrderResponse updateShippingInfo(String id, String trackingNumber);
    
    void deleteOrder(String id);
    
    long countOrdersByUserIdAndStatus(String userId, OrderStatus status);
    
    void markOrderAsDelivered(String id);
    
    OrderResponse processPayment(String id, String transactionId);
} 