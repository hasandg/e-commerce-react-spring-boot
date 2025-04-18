package com.hasandag.ecommerce.notification.messaging;

import com.hasandag.ecommerce.notification.model.Notification;
import com.hasandag.ecommerce.notification.model.NotificationType;
import com.hasandag.ecommerce.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationEventConsumerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationEventConsumer notificationEventConsumer;

    private OrderNotificationEvent orderEvent;
    private UserNotificationEvent userEvent;
    private Notification notification;

    @BeforeEach
    void setUp() {
        // Set up test data
        orderEvent = new OrderNotificationEvent(
                "ORD-12345",
                "user123",
                "Order Confirmation",
                "Your order has been confirmed and is being processed."
        );

        userEvent = new UserNotificationEvent(
                "user123",
                "Welcome",
                "Welcome to our e-commerce platform!",
                NotificationType.EMAIL,
                "user123",
                "USER"
        );

        notification = Notification.builder()
                .id("1")
                .userId("user-123")
                .subject("Test Subject")
                .message("Test Message")
                .type(NotificationType.IN_APP)
                .build();
    }

    @Test
    void handleOrderNotification_ShouldCreateNotification() {
        // Arrange
        when(notificationService.createNotification(
                anyString(), anyString(), anyString(), any(NotificationType.class), anyString(), anyString()))
                .thenReturn(notification);

        // Act
        notificationEventConsumer.handleOrderNotification(orderEvent);

        // Assert
        verify(notificationService).createNotification(
                eq("user123"),
                eq("Order Confirmation"),
                eq("Your order has been confirmed and is being processed."),
                eq(NotificationType.IN_APP),
                eq("ORD-12345"),
                eq("ORDER")
        );
    }

    @Test
    void handleUserNotification_ShouldCreateNotification() {
        // Arrange
        when(notificationService.createNotification(
                anyString(), anyString(), anyString(), any(NotificationType.class), anyString(), anyString()))
                .thenReturn(notification);

        // Act
        notificationEventConsumer.handleUserNotification(userEvent);

        // Assert
        verify(notificationService).createNotification(
                eq("user123"),
                eq("Welcome"),
                eq("Welcome to our e-commerce platform!"),
                eq(NotificationType.EMAIL),
                eq("user123"),
                eq("USER")
        );
    }

    @Test
    void handleOrderNotification_WithNullEvent_ShouldHandleGracefully() {
        // Act
        notificationEventConsumer.handleOrderNotification(null);

        // Assert
        verifyNoInteractions(notificationService);
    }

    @Test
    void handleUserNotification_WithNullEvent_ShouldHandleGracefully() {
        // Act
        notificationEventConsumer.handleUserNotification(null);

        // Assert
        verifyNoInteractions(notificationService);
    }
} 