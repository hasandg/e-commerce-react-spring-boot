package com.hasandag.ecommerce.notification.messaging;

import com.hasandag.ecommerce.notification.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserNotificationEvent {
    private String userId;
    private String subject;
    private String message;
    private NotificationType type;
    private String relatedEntityId;
    private String relatedEntityType;
} 