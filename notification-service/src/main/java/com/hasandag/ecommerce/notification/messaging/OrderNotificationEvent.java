package com.hasandag.ecommerce.notification.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderNotificationEvent {
    private String orderId;
    private String userId;
    private String subject;
    private String message;
} 