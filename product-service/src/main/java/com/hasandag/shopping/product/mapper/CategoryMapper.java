package com.hasandag.ecommerce.product.mapper;

import com.hasandag.ecommerce.product.dto.CategoryRequest;
import com.hasandag.ecommerce.product.dto.CategoryResponse;
import com.hasandag.ecommerce.product.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CategoryMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subCategoryIds", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toEntity(CategoryRequest categoryRequest);
    
    CategoryResponse toDto(Category category);
    
    List<CategoryResponse> toDtoList(List<Category> categories);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subCategoryIds", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateCategoryFromRequest(CategoryRequest categoryRequest, @MappingTarget Category category);
} 