package com.hasandag.ecommerce.notification.mapper;

import com.hasandag.ecommerce.notification.dto.NotificationRequest;
import com.hasandag.ecommerce.notification.dto.NotificationResponse;
import com.hasandag.ecommerce.notification.model.Notification;
import com.hasandag.ecommerce.notification.model.NotificationStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {

    public Notification toEntity(NotificationRequest request) {
        if (request == null) {
            return null;
        }
        
        return Notification.builder()
                .userId(request.getUserId())
                .subject(request.getSubject())
                .message(request.getMessage())
                .type(request.getType())
                .status(NotificationStatus.PENDING)
                .relatedEntityId(request.getRelatedEntityId())
                .relatedEntityType(request.getRelatedEntityType())
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();
    }
    
    public NotificationResponse toResponse(Notification notification) {
        if (notification == null) {
            return null;
        }
        
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .subject(notification.getSubject())
                .message(notification.getMessage())
                .type(notification.getType())
                .status(notification.getStatus())
                .relatedEntityId(notification.getRelatedEntityId())
                .relatedEntityType(notification.getRelatedEntityType())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .sentAt(notification.getSentAt())
                .readAt(notification.getReadAt())
                .build();
    }
    
    public List<NotificationResponse> toResponseList(List<Notification> notifications) {
        if (notifications == null) {
            return Collections.emptyList();
        }
        
        return notifications.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
} 