package com.hasandag.ecommerce.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "order_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    
    @Id
    private String id;
    
    private String orderId;
    
    private String productId;
    
    private String productName;
    
    private String productImageUrl;
    
    private int quantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal subtotal;
} 