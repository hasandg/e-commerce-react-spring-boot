package com.hasandag.ecommerce.product.mapper;

import com.hasandag.ecommerce.product.dto.ProductRequest;
import com.hasandag.ecommerce.product.dto.ProductResponse;
import com.hasandag.ecommerce.product.model.Product;
import com.hasandag.ecommerce.shared.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sellerId", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "reviewCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductRequest productRequest);
    
    ProductResponse toResponse(Product product);
    
    List<ProductResponse> toResponseList(List<Product> products);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateProductFromRequest(ProductRequest productRequest, @MappingTarget Product product);
    
    ProductDto toDto(Product product);
    
    List<ProductDto> toDtoList(List<Product> products);
    
    Product fromDto(ProductDto productDto);
} 