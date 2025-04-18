package com.hasandag.ecommerce.product.repository;

import com.hasandag.ecommerce.product.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    
    Page<Review> findByProductId(String productId, Pageable pageable);
    
    Page<Review> findByUserId(String userId, Pageable pageable);
    
    Optional<Review> findByProductIdAndUserId(String productId, String userId);
    
    @Query("{'productId': ?0, 'approved': true}")
    Page<Review> findApprovedReviewsByProductId(String productId, Pageable pageable);
    
    @Query("{'approved': false}")
    Page<Review> findPendingReviews(Pageable pageable);
    
    List<Review> findByProductIdAndApprovedTrue(String productId);
    
    Page<Review> findByRating(int rating, Pageable pageable);
    
    Page<Review> findByProductIdAndApprovedTrue(String productId, Pageable pageable);
} 