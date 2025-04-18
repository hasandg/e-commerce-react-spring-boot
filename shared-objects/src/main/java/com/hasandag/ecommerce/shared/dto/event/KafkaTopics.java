package com.hasandag.ecommerce.shared.dto.event;

public class KafkaTopics {
    public static final String USER_REGISTERED = "user-registered";
    public static final String ORDER_CREATED = "order-created";
    public static final String PAYMENT_PROCESSED = "payment-processed";
    public static final String ORDER_STATUS_CHANGED = "order-status-changed";
    public static final String PRODUCT_STOCK_CHANGED = "product-stock-changed";
    public static final String NOTIFICATION_EVENT = "notification-event";
    
    private KafkaTopics() {
        // Private constructor to prevent instantiation
    }
} 