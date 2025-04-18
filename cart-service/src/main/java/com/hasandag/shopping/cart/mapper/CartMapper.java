package com.hasandag.ecommerce.cart.mapper;

import com.hasandag.ecommerce.cart.dto.CartItemRequest;
import com.hasandag.ecommerce.cart.dto.CartItemResponse;
import com.hasandag.ecommerce.cart.dto.CartResponse;
import com.hasandag.ecommerce.cart.model.Cart;
import com.hasandag.ecommerce.cart.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cartId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CartItem toCartItem(CartItemRequest request);
    
    @Mapping(target = "subtotal", expression = "java(item.getSubtotal())")
    CartItemResponse toCartItemResponse(CartItem item);
    
    @Mapping(target = "itemCount", expression = "java(cart.getItems().size())")
    CartResponse toCartResponse(Cart cart);
    
    List<CartItemResponse> toCartItemResponseList(List<CartItem> items);
} 