package com.hasandag.ecommerce.product.service;

import com.hasandag.ecommerce.product.dto.ProductRequest;
import com.hasandag.ecommerce.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    
    ProductResponse createProduct(ProductRequest productRequest, String sellerId);
    
    ProductResponse getProductById(String id);
    
    Page<ProductResponse> getAllProducts(Pageable pageable);
    
    Page<ProductResponse> getAvailableProducts(Pageable pageable);
    
    Page<ProductResponse> getProductsByCategory(String categoryId, Pageable pageable);
    
    Page<ProductResponse> getProductsBySeller(String sellerId, Pageable pageable);
    
    Page<ProductResponse> searchProductsByName(String name, Pageable pageable);
    
    Page<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    Page<ProductResponse> getProductsByCategoryAndPriceRange(String categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    ProductResponse updateProduct(String id, ProductRequest productRequest, String sellerId);
    
    void deleteProduct(String id, String sellerId);
    
    ProductResponse updateProductStock(String id, Integer quantity);
    
    void updateProductRating(String productId, double averageRating, int reviewCount);
    
    List<ProductResponse> getNewArrivals();
    
    List<ProductResponse> getTopRatedProducts();
    
    ProductResponse uploadProductImages(String productId, List<MultipartFile> images, String sellerId) throws IOException;
    
    ProductResponse deleteProductImage(String productId, String imageUrl, String sellerId);
} 