package com.hasandag.ecommerce.product.repository;

import com.hasandag.ecommerce.product.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    
    Optional<Category> findByName(String name);
    
    List<Category> findByParentId(String parentId);
    
    @Query("{'parentId': null}")
    List<Category> findAllRootCategories();
    
    @Query("{'active': true}")
    List<Category> findAllActiveCategories();
    
    @Query("{'parentId': null}")
    List<Category> findByParentIdIsNull();
} 