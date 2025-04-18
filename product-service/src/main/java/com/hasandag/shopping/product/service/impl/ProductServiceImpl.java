package com.hasandag.ecommerce.product.service.impl;

import com.hasandag.ecommerce.product.dto.ProductRequest;
import com.hasandag.ecommerce.product.dto.ProductResponse;
import com.hasandag.ecommerce.product.exception.ProductNotFoundException;
import com.hasandag.ecommerce.product.exception.UnauthorizedAccessException;
import com.hasandag.ecommerce.product.mapper.ProductMapper;
import com.hasandag.ecommerce.product.model.Product;
import com.hasandag.ecommerce.product.repository.CategoryRepository;
import com.hasandag.ecommerce.product.repository.ProductRepository;
import com.hasandag.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    
    @Value("${app.upload.dir:uploads/products}")
    private String uploadDir;
    
    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest, String sellerId) {
        log.info("Creating product with name: {}, sellerId: {}", productRequest.getName(), sellerId);
        
        // Verify if category exists
        categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        
        Product product = productMapper.toEntity(productRequest);
        product.setSellerId(sellerId);
        
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with id: {}", savedProduct.getId());
        
        return productMapper.toResponse(savedProduct);
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public ProductResponse updateProduct(String id, ProductRequest productRequest, String sellerId) {
        log.info("Updating product with id: {}, sellerId: {}", id, sellerId);
        
        Product product = getProductEntityById(id);
        
        // Verify seller ownership
        if (!product.getSellerId().equals(sellerId)) {
            throw new UnauthorizedAccessException("You don't have permission to update this product");
        }
        
        // Verify if new category exists (if changed)
        if (!product.getCategoryId().equals(productRequest.getCategoryId())) {
            categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        }
        
        productMapper.updateProductFromRequest(productRequest, product);
        Product updatedProduct = productRepository.save(product);
        
        log.info("Product updated successfully with id: {}", updatedProduct.getId());
        return productMapper.toResponse(updatedProduct);
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(String id, String sellerId) {
        log.info("Deleting product with id: {}, sellerId: {}", id, sellerId);
        
        Product product = getProductEntityById(id);
        
        // Verify seller ownership
        if (!product.getSellerId().equals(sellerId)) {
            throw new UnauthorizedAccessException("You don't have permission to delete this product");
        }
        
        productRepository.deleteById(id);
        log.info("Product deleted successfully with id: {}", id);
    }
    
    @Override
    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProductById(String id) {
        log.info("Getting product with id: {}", id);
        return productMapper.toResponse(getProductEntityById(id));
    }
    
    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        log.info("Getting all products with page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(productMapper::toResponse);
    }
    
    @Override
    public Page<ProductResponse> getAvailableProducts(Pageable pageable) {
        log.info("Getting available products with page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Product> products = productRepository.findAllAvailableProducts(pageable);
        return products.map(productMapper::toResponse);
    }
    
    @Override
    @Cacheable(value = "productsByCategory", key = "{#categoryId, #pageable.pageNumber, #pageable.pageSize}")
    public Page<ProductResponse> getProductsByCategory(String categoryId, Pageable pageable) {
        log.info("Getting products by category id: {}", categoryId);
        Page<Product> products = productRepository.findByCategoryId(categoryId, pageable);
        return products.map(productMapper::toResponse);
    }
    
    @Override
    public Page<ProductResponse> getProductsBySeller(String sellerId, Pageable pageable) {
        log.info("Getting products by seller id: {}", sellerId);
        Page<Product> products = productRepository.findBySellerId(sellerId, pageable);
        return products.map(productMapper::toResponse);
    }
    
    @Override
    public Page<ProductResponse> searchProductsByName(String name, Pageable pageable) {
        log.info("Searching products by name: {}", name);
        Page<Product> products = productRepository.findByNameContainingIgnoreCase(name, pageable);
        return products.map(productMapper::toResponse);
    }
    
    @Override
    public Page<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        log.info("Getting products by price range: {} - {}", minPrice, maxPrice);
        Page<Product> products = productRepository.findByPriceRange(minPrice, maxPrice, pageable);
        return products.map(productMapper::toResponse);
    }
    
    @Override
    public Page<ProductResponse> getProductsByCategoryAndPriceRange(String categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        log.info("Getting products by category id: {} and price range: {} - {}", categoryId, minPrice, maxPrice);
        Page<Product> products = productRepository.findByCategoryIdAndPriceRange(categoryId, minPrice, maxPrice, pageable);
        return products.map(productMapper::toResponse);
    }
    
    @Override
    @Cacheable(value = "newArrivals")
    public List<ProductResponse> getNewArrivals() {
        log.info("Getting new arrivals");
        List<Product> products = productRepository.findTop10ByOrderByCreatedAtDesc();
        return productMapper.toResponseList(products);
    }
    
    @Override
    @Cacheable(value = "topRatedProducts")
    public List<ProductResponse> getTopRatedProducts() {
        log.info("Getting top rated products");
        List<Product> products = productRepository.findTop10ByOrderByAverageRatingDesc();
        return productMapper.toResponseList(products);
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public ProductResponse updateProductStock(String id, Integer quantity) {
        log.info("Updating product stock with id: {}, quantity: {}", id, quantity);
        
        Product product = getProductEntityById(id);
        
        // Update stock
        int newStock = product.getStockQuantity() + quantity;
        if (newStock < 0) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        
        product.setStockQuantity(newStock);
        Product updatedProduct = productRepository.save(product);
        
        log.info("Product stock updated successfully with id: {}, new stock: {}", id, newStock);
        return productMapper.toResponse(updatedProduct);
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "products", key = "#productId")
    public void updateProductRating(String productId, double averageRating, int reviewCount) {
        log.info("Updating product rating with id: {}, rating: {}, reviewCount: {}", productId, averageRating, reviewCount);
        
        Product product = getProductEntityById(productId);
        
        product.setAverageRating(averageRating);
        product.setReviewCount(reviewCount);
        productRepository.save(product);
        
        log.info("Product rating updated successfully with id: {}", productId);
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "products", key = "#productId")
    public ProductResponse uploadProductImages(String productId, List<MultipartFile> images, String sellerId) throws IOException {
        log.info("Uploading images for product with id: {}, sellerId: {}", productId, sellerId);
        
        Product product = getProductEntityById(productId);
        
        // Verify seller ownership
        if (!product.getSellerId().equals(sellerId)) {
            throw new UnauthorizedAccessException("You don't have permission to update this product");
        }
        
        List<String> imageUrls = new ArrayList<>();
        
        // Create upload directory if it doesn't exist
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        // Process each image
        for (MultipartFile image : images) {
            // Generate unique filename
            String originalFilename = image.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + extension;
            
            // Save to filesystem
            Path targetPath = Paths.get(uploadDir, newFilename);
            Files.copy(image.getInputStream(), targetPath);
            
            // Add to image URLs
            String imageUrl = "/api/products/images/" + newFilename;
            imageUrls.add(imageUrl);
        }
        
        // Update product with new image URLs
        if (product.getImageUrls() == null) {
            product.setImageUrls(new ArrayList<>());
        }
        product.getImageUrls().addAll(imageUrls);
        
        Product updatedProduct = productRepository.save(product);
        log.info("Images uploaded successfully for product with id: {}", productId);
        
        return productMapper.toResponse(updatedProduct);
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "products", key = "#productId")
    public ProductResponse deleteProductImage(String productId, String imageUrl, String sellerId) {
        log.info("Deleting image for product with id: {}, sellerId: {}", productId, sellerId);
        
        Product product = getProductEntityById(productId);
        
        // Verify seller ownership
        if (!product.getSellerId().equals(sellerId)) {
            throw new UnauthorizedAccessException("You don't have permission to update this product");
        }
        
        // Verify image exists for this product
        if (product.getImageUrls() == null || !product.getImageUrls().contains(imageUrl)) {
            throw new IllegalArgumentException("Image not found for this product");
        }
        
        // Remove image from database
        product.getImageUrls().remove(imageUrl);
        Product updatedProduct = productRepository.save(product);
        
        // Try to remove file from filesystem if it's a local file
        if (imageUrl.startsWith("/api/products/images/")) {
            String filename = imageUrl.substring("/api/products/images/".length());
            try {
                Path filePath = Paths.get(uploadDir, filename);
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                log.error("Failed to delete image file: {}", e.getMessage());
                // Continue execution even if file deletion fails
            }
        }
        
        log.info("Image deleted successfully for product with id: {}", productId);
        return productMapper.toResponse(updatedProduct);
    }
    
    // Helper method to get product entity by id
    private Product getProductEntityById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }
} 