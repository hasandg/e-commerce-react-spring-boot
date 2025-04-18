package com.hasandag.ecommerce.order.controller;

import com.hasandag.ecommerce.order.dto.OrderRequest;
import com.hasandag.ecommerce.order.dto.OrderResponse;
import com.hasandag.ecommerce.order.model.OrderStatus;
import com.hasandag.ecommerce.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest orderRequest,
            @RequestParam List<String> cartItemIds) {
        log.info("Creating order for user: {}", orderRequest.getUserId());
        OrderResponse createdOrder = orderService.createOrder(orderRequest, cartItemIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String id) {
        log.info("Fetching order with ID: {}", id);
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<OrderResponse>> getOrdersByUserId(
            @PathVariable String userId,
            Pageable pageable) {
        log.info("Fetching orders for user: {}", userId);
        Page<OrderResponse> orders = orderService.getOrdersByUserId(userId, pageable);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/user/{userId}/order/{id}")
    public ResponseEntity<OrderResponse> getOrderByIdAndUserId(
            @PathVariable String id,
            @PathVariable String userId) {
        log.info("Fetching order with ID: {} for user: {}", id, userId);
        OrderResponse order = orderService.getOrderByIdAndUserId(id, userId);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderResponse> getOrderByOrderNumber(@PathVariable String orderNumber) {
        log.info("Fetching order with order number: {}", orderNumber);
        OrderResponse order = orderService.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@PathVariable OrderStatus status) {
        log.info("Fetching orders with status: {}", status);
        List<OrderResponse> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserIdAndStatus(
            @PathVariable String userId,
            @PathVariable OrderStatus status) {
        log.info("Fetching orders for user: {} with status: {}", userId, status);
        List<OrderResponse> orders = orderService.getOrdersByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<OrderResponse>> getOrdersBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("Fetching orders between dates: {} and {}", startDate, endDate);
        List<OrderResponse> orders = orderService.getOrdersBetweenDates(startDate, endDate);
        return ResponseEntity.ok(orders);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable String id,
            @RequestParam OrderStatus status) {
        log.info("Updating status to {} for order with ID: {}", status, id);
        OrderResponse updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @PutMapping("/{id}/shipping")
    public ResponseEntity<OrderResponse> updateShippingInfo(
            @PathVariable String id,
            @RequestParam String trackingNumber) {
        log.info("Updating shipping info for order with ID: {}", id);
        OrderResponse updatedOrder = orderService.updateShippingInfo(id, trackingNumber);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @PutMapping("/{id}/payment")
    public ResponseEntity<OrderResponse> processPayment(
            @PathVariable String id,
            @RequestParam String transactionId) {
        log.info("Processing payment for order with ID: {}", id);
        OrderResponse updatedOrder = orderService.processPayment(id, transactionId);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @PutMapping("/{id}/deliver")
    public ResponseEntity<Void> markOrderAsDelivered(@PathVariable String id) {
        log.info("Marking order as delivered with ID: {}", id);
        orderService.markOrderAsDelivered(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        log.info("Deleting order with ID: {}", id);
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/user/{userId}/count/{status}")
    public ResponseEntity<Long> countOrdersByUserIdAndStatus(
            @PathVariable String userId,
            @PathVariable OrderStatus status) {
        log.info("Counting orders for user: {} with status: {}", userId, status);
        long count = orderService.countOrdersByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(count);
    }
} 