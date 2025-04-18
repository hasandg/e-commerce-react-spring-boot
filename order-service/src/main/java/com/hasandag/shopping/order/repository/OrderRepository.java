package com.hasandag.ecommerce.order.repository;

import com.hasandag.ecommerce.order.model.Order;
import com.hasandag.ecommerce.order.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    
    Page<Order> findByUserId(String userId, Pageable pageable);
    
    Optional<Order> findByOrderNumber(String orderNumber);
    
    Optional<Order> findByIdAndUserId(String id, String userId);
    
    List<Order> findByStatus(OrderStatus status);
    
    List<Order> findByUserIdAndStatus(String userId, OrderStatus status);
    
    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    long countByUserIdAndStatus(String userId, OrderStatus status);
} 