package com.hasandag.ecommerce.product.controller;

import com.hasandag.ecommerce.product.dto.ProductRequest;
import com.hasandag.ecommerce.product.dto.ProductResponse;
import com.hasandag.ecommerce.product.service.ProductService;
import com.hasandag.ecommerce.shared.dto.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "Endpoints for managing products")
public class ProductController {
    
    private final ProductService productService;
    
    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a new product", description = "Creates a new product with the given details")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest productRequest,
            @AuthenticationPrincipal Jwt jwt) {
        
        String sellerId = jwt.getSubject();
        ProductResponse createdProduct = productService.createProduct(productRequest, sellerId);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", createdProduct));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update a product", description = "Updates an existing product with the given details")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductRequest productRequest,
            @AuthenticationPrincipal Jwt jwt) {
        
        String sellerId = jwt.getSubject();
        ProductResponse updatedProduct = productService.updateProduct(id, productRequest, sellerId);
        
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", updatedProduct));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete a product", description = "Deletes an existing product")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt) {
        
        String sellerId = jwt.getSubject();
        productService.deleteProduct(id, sellerId);
        
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID", description = "Retrieves a product by its ID")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable String id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }
    
    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves all products with pagination")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<ProductResponse> products = productService.getAllProducts(pageRequest);
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get products by category", description = "Retrieves all products in a specific category")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsByCategory(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ProductResponse> products = productService.getProductsByCategory(categoryId, pageRequest);
        
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    @GetMapping("/seller/{sellerId}")
    @Operation(summary = "Get products by seller", description = "Retrieves all products from a specific seller")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsBySeller(
            @PathVariable String sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ProductResponse> products = productService.getProductsBySeller(sellerId, pageRequest);
        
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search products by name", description = "Searches for products containing the given name")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchProductsByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ProductResponse> products = productService.searchProductsByName(name, pageRequest);
        
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    @GetMapping("/price-range")
    @Operation(summary = "Get products by price range", description = "Retrieves products within the specified price range")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ProductResponse> products = productService.getProductsByPriceRange(minPrice, maxPrice, pageRequest);
        
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    @GetMapping("/category/{categoryId}/price-range")
    @Operation(summary = "Get products by category and price range", description = "Retrieves products in a specific category and within the specified price range")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsByCategoryAndPriceRange(
            @PathVariable String categoryId,
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ProductResponse> products = productService.getProductsByCategoryAndPriceRange(categoryId, minPrice, maxPrice, pageRequest);
        
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    @GetMapping("/new-arrivals")
    @Operation(summary = "Get new arrivals", description = "Retrieves the 10 most recently added products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getNewArrivals() {
        List<ProductResponse> products = productService.getNewArrivals();
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    @GetMapping("/top-rated")
    @Operation(summary = "Get top rated products", description = "Retrieves the 10 highest rated products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getTopRatedProducts() {
        List<ProductResponse> products = productService.getTopRatedProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('SELLER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Upload product images", description = "Uploads one or more images for a product")
    public ResponseEntity<ApiResponse<ProductResponse>> uploadProductImages(
            @PathVariable String id,
            @RequestParam("images") List<MultipartFile> images,
            @AuthenticationPrincipal Jwt jwt) throws IOException {
        
        String sellerId = jwt.getSubject();
        ProductResponse updatedProduct = productService.uploadProductImages(id, images, sellerId);
        
        return ResponseEntity.ok(ApiResponse.success("Product images uploaded successfully", updatedProduct));
    }
    
    @DeleteMapping("/{productId}/images/{imageUrl}")
    @PreAuthorize("hasRole('SELLER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete product image", description = "Deletes a specific image from a product")
    public ResponseEntity<ApiResponse<ProductResponse>> deleteProductImage(
            @PathVariable String productId,
            @PathVariable String imageUrl,
            @AuthenticationPrincipal Jwt jwt) {
        
        String sellerId = jwt.getSubject();
        ProductResponse updatedProduct = productService.deleteProductImage(productId, imageUrl, sellerId);
        
        return ResponseEntity.ok(ApiResponse.success("Product image deleted successfully", updatedProduct));
    }
    
    @GetMapping("/images/{filename:.+}")
    @Operation(summary = "Get product image", description = "Retrieves a product image by filename")
    public ResponseEntity<byte[]> getProductImage(@PathVariable String filename) throws IOException {
        String uploadDir = System.getProperty("app.upload.dir", "uploads/products");
        Path imagePath = Paths.get(uploadDir, filename);
        
        if (!Files.exists(imagePath)) {
            return ResponseEntity.notFound().build();
        }
        
        byte[] imageBytes = Files.readAllBytes(imagePath);
        
        // Determine content type based on file extension
        String contentType = determineContentType(filename);
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(imageBytes);
    }
    
    private String determineContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            default:
                return "application/octet-stream";
        }
    }
} 