package com.hasandag.ecommerce.product.service.impl;

import com.hasandag.ecommerce.product.dto.CategoryRequest;
import com.hasandag.ecommerce.product.dto.CategoryResponse;
import com.hasandag.ecommerce.product.exception.CategoryNotFoundException;
import com.hasandag.ecommerce.product.mapper.CategoryMapper;
import com.hasandag.ecommerce.product.model.Category;
import com.hasandag.ecommerce.product.repository.CategoryRepository;
import com.hasandag.ecommerce.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category category = categoryMapper.toEntity(categoryRequest);
        Category savedCategory = categoryRepository.save(category);
        log.info("Category created with ID: {}", savedCategory.getId());
        
        // If this category has a parent, update the parent's subcategory list
        if (category.getParentId() != null && !category.getParentId().isEmpty()) {
            addSubCategory(category.getParentId(), savedCategory.getId());
        }
        
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryResponse getCategoryById(String id) {
        Category category = findCategoryById(id);
        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getMainCategories() {
        List<Category> mainCategories = categoryRepository.findByParentIdIsNull();
        return mainCategories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getSubCategories(String parentId) {
        Category parentCategory = findCategoryById(parentId);
        List<Category> subCategories = new ArrayList<>();
        
        for (String subCategoryId : parentCategory.getSubCategoryIds()) {
            categoryRepository.findById(subCategoryId)
                    .ifPresent(subCategories::add);
        }
        
        return subCategories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse updateCategory(String id, CategoryRequest categoryRequest) {
        Category existingCategory = findCategoryById(id);
        
        existingCategory.setName(categoryRequest.getName());
        existingCategory.setDescription(categoryRequest.getDescription());
        existingCategory.setImageUrl(categoryRequest.getImageUrl());
        existingCategory.setActive(categoryRequest.isActive());
        
        // Handle parent category change if needed
        if (categoryRequest.getParentId() != null && 
                !categoryRequest.getParentId().equals(existingCategory.getParentId())) {
            
            // If previously had a parent, remove from that parent's subcategories
            if (existingCategory.getParentId() != null) {
                removeSubCategory(existingCategory.getParentId(), id);
            }
            
            // Set new parent ID
            existingCategory.setParentId(categoryRequest.getParentId());
            
            // Add to new parent's subcategories
            if (!categoryRequest.getParentId().isEmpty()) {
                addSubCategory(categoryRequest.getParentId(), id);
            }
        }
        
        Category updatedCategory = categoryRepository.save(existingCategory);
        log.info("Category updated with ID: {}", updatedCategory.getId());
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    public void deleteCategory(String id) {
        Category category = findCategoryById(id);
        
        // If this category has a parent, update the parent's subcategory list
        if (category.getParentId() != null && !category.getParentId().isEmpty()) {
            removeSubCategory(category.getParentId(), id);
        }
        
        // If this category has subcategories, make them top-level categories
        if (!category.getSubCategoryIds().isEmpty()) {
            for (String subCategoryId : category.getSubCategoryIds()) {
                categoryRepository.findById(subCategoryId).ifPresent(subCategory -> {
                    subCategory.setParentId(null);
                    categoryRepository.save(subCategory);
                });
            }
        }
        
        categoryRepository.delete(category);
        log.info("Category deleted with ID: {}", id);
    }

    @Override
    public void addSubCategory(String parentId, String subCategoryId) {
        Category parentCategory = findCategoryById(parentId);
        if (!parentCategory.getSubCategoryIds().contains(subCategoryId)) {
            parentCategory.getSubCategoryIds().add(subCategoryId);
            categoryRepository.save(parentCategory);
            log.info("Subcategory {} added to parent category {}", subCategoryId, parentId);
        }
    }

    @Override
    public void removeSubCategory(String parentId, String subCategoryId) {
        Category parentCategory = findCategoryById(parentId);
        if (parentCategory.getSubCategoryIds().contains(subCategoryId)) {
            parentCategory.getSubCategoryIds().remove(subCategoryId);
            categoryRepository.save(parentCategory);
            log.info("Subcategory {} removed from parent category {}", subCategoryId, parentId);
        }
    }
    
    @Override
    public List<CategoryResponse> getSubcategories(String parentId) {
        return getSubCategories(parentId);
    }
    
    @Override
    public List<CategoryResponse> getRootCategories() {
        return getMainCategories();
    }

    private Category findCategoryById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }
} 