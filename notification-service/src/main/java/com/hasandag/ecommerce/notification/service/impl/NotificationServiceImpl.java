package com.hasandag.ecommerce.notification.service.impl;

import com.hasandag.ecommerce.notification.exception.ResourceNotFoundException;
import com.hasandag.ecommerce.notification.model.Notification;
import com.hasandag.ecommerce.notification.model.NotificationStatus;
import com.hasandag.ecommerce.notification.model.NotificationType;
import com.hasandag.ecommerce.notification.repository.NotificationRepository;
import com.hasandag.ecommerce.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public Notification sendNotification(Notification notification) {
        notification.setStatus(NotificationStatus.SENT);
        notification.setSentAt(LocalDateTime.now());
        log.info("Sending notification: {}", notification);
        return notificationRepository.save(notification);
    }

    @Override
    public Page<Notification> getUserNotifications(String userId, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Override
    public List<Notification> getUnreadNotifications(String userId) {
        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional
    public Notification markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId.toString()));
        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public List<Notification> markAllAsRead(String userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
        LocalDateTime now = LocalDateTime.now();
        unreadNotifications.forEach(notification -> {
            notification.setRead(true);
            notification.setReadAt(now);
        });
        return notificationRepository.saveAll(unreadNotifications);
    }

    @Override
    public long getUnreadCount(String userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }

    @Override
    @Transactional
    public void deleteNotification(String notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new ResourceNotFoundException("Notification", "id", notificationId.toString());
        }
        notificationRepository.deleteById(notificationId);
    }

    @Override
    @Transactional
    public Notification createNotification(String userId, String subject, String message, 
                                          NotificationType type, String relatedEntityId, 
                                          String relatedEntityType) {
        Notification notification = Notification.builder()
                .userId(userId)
                .subject(subject)
                .message(message)
                .type(type)
                .status(NotificationStatus.PENDING)
                .relatedEntityId(relatedEntityId)
                .relatedEntityType(relatedEntityType)
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();
        
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotificationsByStatus(String userId, NotificationStatus status) {
        return notificationRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status);
    }
} 