package com.hasandag.ecommerce.notification.service;

import com.hasandag.ecommerce.notification.model.Notification;
import com.hasandag.ecommerce.notification.model.NotificationStatus;
import com.hasandag.ecommerce.notification.model.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

    Notification saveNotification(Notification notification);
    
    Notification sendNotification(Notification notification);
    
    Page<Notification> getUserNotifications(String userId, Pageable pageable);
    
    List<Notification> getUnreadNotifications(String userId);
    
    Notification markAsRead(String notificationId);
    
    List<Notification> markAllAsRead(String userId);
    
    long getUnreadCount(String userId);
    
    void deleteNotification(String notificationId);
    
    Notification createNotification(String userId, String subject, String message, 
                                    NotificationType type, String relatedEntityId, 
                                    String relatedEntityType);
                                    
    List<Notification> getNotificationsByStatus(String userId, NotificationStatus status);
} 