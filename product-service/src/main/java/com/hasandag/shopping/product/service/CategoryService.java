package com.hasandag.ecommerce.product.service;

import com.hasandag.ecommerce.product.dto.CategoryRequest;
import com.hasandag.ecommerce.product.dto.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    
    CategoryResponse createCategory(CategoryRequest categoryRequest);
    
    CategoryResponse getCategoryById(String id);
    
    List<CategoryResponse> getAllCategories();
    
    List<CategoryResponse> getMainCategories();
    
    List<CategoryResponse> getSubCategories(String parentId);
    
    CategoryResponse updateCategory(String id, CategoryRequest categoryRequest);
    
    void deleteCategory(String id);
    
    void addSubCategory(String parentId, String subCategoryId);
    
    void removeSubCategory(String parentId, String subCategoryId);
    
    List<CategoryResponse> getSubcategories(String parentId);
    
    List<CategoryResponse> getRootCategories();
} 