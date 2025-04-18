package com.hasandag.ecommerce.product.repository;

import com.hasandag.ecommerce.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    
    Page<Product> findByCategoryId(String categoryId, Pageable pageable);
    
    Page<Product> findBySellerId(String sellerId, Pageable pageable);
    
    @Query("{'name': {$regex: ?0, $options: 'i'}}")
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    @Query("{'active': true, 'stockQuantity': {$gt: 0}}")
    Page<Product> findAllAvailableProducts(Pageable pageable);
    
    @Query("{'active': true, 'stockQuantity': {$gt: 0}, 'price': {$gte: ?0, $lte: ?1}}")
    Page<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    @Query("{'active': true, 'stockQuantity': {$gt: 0}, 'categoryId': ?0, 'price': {$gte: ?1, $lte: ?2}}")
    Page<Product> findByCategoryIdAndPriceRange(String categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    List<Product> findTop10ByOrderByCreatedAtDesc();
    
    List<Product> findTop10ByOrderByAverageRatingDesc();
} 