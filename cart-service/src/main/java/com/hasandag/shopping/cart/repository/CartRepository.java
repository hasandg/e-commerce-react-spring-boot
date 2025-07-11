package com.hasandag.ecommerce.cart.repository;

import com.hasandag.ecommerce.cart.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    
    Optional<Cart> findByUserId(String userId);
    
    boolean existsByUserId(String userId);
    
    void deleteByUserId(String userId);
} 