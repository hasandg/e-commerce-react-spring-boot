package com.hasandag.ecommerce.order.service.impl;

import com.hasandag.ecommerce.order.dto.OrderRequest;
import com.hasandag.ecommerce.order.dto.OrderResponse;
import com.hasandag.ecommerce.order.exception.OrderNotFoundException;
import com.hasandag.ecommerce.order.mapper.OrderMapper;
import com.hasandag.ecommerce.order.model.Order;
import com.hasandag.ecommerce.order.model.OrderStatus;
import com.hasandag.ecommerce.order.model.PaymentStatus;
import com.hasandag.ecommerce.order.repository.OrderRepository;
import com.hasandag.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest, List<String> cartItemIds) {
        Order order = orderMapper.toOrder(orderRequest);
        
        // Generate unique order number
        order.setOrderNumber(generateOrderNumber());
        
        // Set initial order status
        order.setStatus(OrderStatus.PENDING);
        
        // Items will be populated from cart service
        // We'll assume this is done by a cart service client
        
        // Calculate order totals
        order.calculateTotals();
        
        Order savedOrder = orderRepository.save(order);
        log.info("Created order with ID: {} for user: {}", savedOrder.getId(), orderRequest.getUserId());
        
        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(String id) {
        Order order = findOrderById(id);
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse getOrderByIdAndUserId(String id, String userId) {
        Order order = orderRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id + " for user: " + userId));
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with order number: " + orderNumber));
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public Page<OrderResponse> getOrdersByUserId(String userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable)
                .map(orderMapper::toOrderResponse);
    }

    @Override
    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        List<Order> orders = orderRepository.findByStatus(status);
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrdersByUserIdAndStatus(String userId, OrderStatus status) {
        List<Order> orders = orderRepository.findByUserIdAndStatus(userId, status);
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrdersBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = orderRepository.findByCreatedAtBetween(startDate, endDate);
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(String id, OrderStatus status) {
        Order order = findOrderById(id);
        order.setStatus(status);
        
        // Set timestamps based on status
        switch (status) {
            case SHIPPED:
                order.setShippedAt(LocalDateTime.now());
                break;
            case DELIVERED:
                order.setDeliveredAt(LocalDateTime.now());
                break;
            default:
                break;
        }
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Updated order status to {} for order ID: {}", status, id);
        
        return orderMapper.toOrderResponse(updatedOrder);
    }

    @Override
    @Transactional
    public OrderResponse updateShippingInfo(String id, String trackingNumber) {
        Order order = findOrderById(id);
        order.setTrackingNumber(trackingNumber);
        order.setStatus(OrderStatus.SHIPPED);
        order.setShippedAt(LocalDateTime.now());
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Updated shipping info for order ID: {}, tracking number: {}", id, trackingNumber);
        
        return orderMapper.toOrderResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(String id) {
        Order order = findOrderById(id);
        orderRepository.delete(order);
        log.info("Deleted order with ID: {}", id);
    }

    @Override
    public long countOrdersByUserIdAndStatus(String userId, OrderStatus status) {
        return orderRepository.countByUserIdAndStatus(userId, status);
    }

    @Override
    @Transactional
    public void markOrderAsDelivered(String id) {
        Order order = findOrderById(id);
        order.setStatus(OrderStatus.DELIVERED);
        order.setDeliveredAt(LocalDateTime.now());
        
        orderRepository.save(order);
        log.info("Marked order as delivered with ID: {}", id);
    }

    @Override
    @Transactional
    public OrderResponse processPayment(String id, String transactionId) {
        Order order = findOrderById(id);
        
        // Update payment details
        order.getPaymentDetails().setTransactionId(transactionId);
        order.getPaymentDetails().setPaymentStatus(PaymentStatus.COMPLETED);
        order.getPaymentDetails().setPaymentDate(LocalDateTime.now());
        
        // Update order status
        order.setStatus(OrderStatus.PAID);
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Processed payment for order ID: {}, transaction ID: {}", id, transactionId);
        
        return orderMapper.toOrderResponse(updatedOrder);
    }
    
    private Order findOrderById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
    }
    
    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
} 