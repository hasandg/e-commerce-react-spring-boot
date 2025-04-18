package com.hasandag.ecommerce.notification.messaging;

import com.hasandag.ecommerce.notification.model.NotificationType;
import com.hasandag.ecommerce.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "order-notifications", groupId = "notification-service-group")
    public void handleOrderNotification(OrderNotificationEvent event) {
        if (event == null) {
            log.warn("Received null order notification event");
            return;
        }
        
        log.info("Received order notification event: {}", event);
        
        notificationService.createNotification(
                event.getUserId(),
                event.getSubject(),
                event.getMessage(),
                NotificationType.IN_APP,
                event.getOrderId(),
                "ORDER"
        );
    }

    @KafkaListener(topics = "user-notifications", groupId = "notification-service-group")
    public void handleUserNotification(UserNotificationEvent event) {
        if (event == null) {
            log.warn("Received null user notification event");
            return;
        }
        
        log.info("Received user notification event: {}", event);
        
        notificationService.createNotification(
                event.getUserId(),
                event.getSubject(),
                event.getMessage(),
                event.getType(),
                event.getRelatedEntityId(),
                event.getRelatedEntityType()
        );
    }
} 