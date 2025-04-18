package com.hasandag.ecommerce.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    private String subject;
    
    private String message;
    
    private NotificationType type;
    
    @Builder.Default
    private NotificationStatus status = NotificationStatus.PENDING;
    
    private String relatedEntityId;
    
    private String relatedEntityType;
    
    @Builder.Default
    private boolean read = false;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    private LocalDateTime sentAt;
    
    private LocalDateTime readAt;
} 