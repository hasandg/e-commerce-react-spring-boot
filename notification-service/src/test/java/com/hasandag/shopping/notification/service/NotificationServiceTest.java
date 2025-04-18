package com.hasandag.ecommerce.notification.service;

import com.hasandag.ecommerce.notification.exception.ResourceNotFoundException;
import com.hasandag.ecommerce.notification.model.Notification;
import com.hasandag.ecommerce.notification.model.NotificationStatus;
import com.hasandag.ecommerce.notification.model.NotificationType;
import com.hasandag.ecommerce.notification.repository.NotificationRepository;
import com.hasandag.ecommerce.notification.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Notification notification;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        notification = Notification.builder()
                .id("1")
                .userId("user123")
                .subject("Test Subject")
                .message("Test Message")
                .type(NotificationType.IN_APP)
                .status(NotificationStatus.PENDING)
                .relatedEntityId("TEST-123")
                .relatedEntityType("TEST")
                .read(false)
                .createdAt(now)
                .build();
    }

    @Test
    void saveNotification_ShouldReturnSavedNotification() {
        // Arrange
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // Act
        Notification result = notificationService.saveNotification(notification);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("user123", result.getUserId());
        assertEquals("Test Subject", result.getSubject());
        verify(notificationRepository).save(notification);
    }

    @Test
    void sendNotification_ShouldUpdateStatusAndSentAt() {
        // Arrange
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification notif = invocation.getArgument(0);
            return notif;
        });

        // Act
        Notification result = notificationService.sendNotification(notification);

        // Assert
        assertNotNull(result);
        assertEquals(NotificationStatus.SENT, result.getStatus());
        assertNotNull(result.getSentAt());
        verify(notificationRepository).save(notification);
    }

    @Test
    void getUserNotifications_ShouldReturnPageOfNotifications() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Notification> expectedPage = new PageImpl<>(
                Collections.singletonList(notification), pageable, 1);
        
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc("user123", pageable))
                .thenReturn(expectedPage);

        // Act
        Page<Notification> result = notificationService.getUserNotifications("user123", pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(notification, result.getContent().get(0));
        verify(notificationRepository).findByUserIdOrderByCreatedAtDesc("user123", pageable);
    }

    @Test
    void getUnreadNotifications_ShouldReturnUnreadNotifications() {
        // Arrange
        when(notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc("user123"))
                .thenReturn(Collections.singletonList(notification));

        // Act
        List<Notification> result = notificationService.getUnreadNotifications("user123");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(notification, result.get(0));
        verify(notificationRepository).findByUserIdAndReadFalseOrderByCreatedAtDesc("user123");
    }

    @Test
    void markAsRead_ShouldUpdateReadStatusAndReadAt() {
        // Arrange
        when(notificationRepository.findById("1")).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification notif = invocation.getArgument(0);
            return notif;
        });

        // Act
        Notification result = notificationService.markAsRead("1");

        // Assert
        assertNotNull(result);
        assertTrue(result.isRead());
        assertNotNull(result.getReadAt());
        verify(notificationRepository).findById("1");
        verify(notificationRepository).save(notification);
    }

    @Test
    void markAsRead_WhenNotificationNotFound_ShouldThrowException() {
        // Arrange
        when(notificationRepository.findById("999")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            notificationService.markAsRead("999");
        });
        
        assertEquals("Notification not found with id: '999'", exception.getMessage());
        verify(notificationRepository).findById("999");
        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    void markAllAsRead_ShouldUpdateAllUnreadNotifications() {
        // Arrange
        List<Notification> unreadNotifications = Collections.singletonList(notification);
        when(notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc("user123"))
                .thenReturn(unreadNotifications);
        when(notificationRepository.saveAll(anyList())).thenAnswer(invocation -> {
            List<Notification> notifs = invocation.getArgument(0);
            return notifs;
        });

        // Act
        List<Notification> result = notificationService.markAllAsRead("user123");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isRead());
        assertNotNull(result.get(0).getReadAt());
        verify(notificationRepository).findByUserIdAndReadFalseOrderByCreatedAtDesc("user123");
        verify(notificationRepository).saveAll(anyList());
    }

    @Test
    void getUnreadCount_ShouldReturnCount() {
        // Arrange
        when(notificationRepository.countByUserIdAndReadFalse("user123")).thenReturn(5L);

        // Act
        long result = notificationService.getUnreadCount("user123");

        // Assert
        assertEquals(5L, result);
        verify(notificationRepository).countByUserIdAndReadFalse("user123");
    }

    @Test
    void deleteNotification_WhenNotificationExists_ShouldDelete() {
        // Arrange
        when(notificationRepository.existsById("1")).thenReturn(true);
        doNothing().when(notificationRepository).deleteById("1");

        // Act
        notificationService.deleteNotification("1");

        // Assert
        verify(notificationRepository).existsById("1");
        verify(notificationRepository).deleteById("1");
    }

    @Test
    void deleteNotification_WhenNotificationNotFound_ShouldThrowException() {
        // Arrange
        when(notificationRepository.existsById("999")).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            notificationService.deleteNotification("999");
        });
        
        assertEquals("Notification not found with id: '999'", exception.getMessage());
        verify(notificationRepository).existsById("999");
        verify(notificationRepository, never()).deleteById(anyString());
    }

    @Test
    void createNotification_ShouldCreateAndReturnNotification() {
        // Arrange
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification notif = invocation.getArgument(0);
            notif.setId("1");
            return notif;
        });

        // Act
        Notification result = notificationService.createNotification(
                "user123", "Test Subject", "Test Message", 
                NotificationType.IN_APP, "TEST-123", "TEST");

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("user123", result.getUserId());
        assertEquals("Test Subject", result.getSubject());
        assertEquals("Test Message", result.getMessage());
        assertEquals(NotificationType.IN_APP, result.getType());
        assertEquals("TEST-123", result.getRelatedEntityId());
        assertEquals("TEST", result.getRelatedEntityType());
        assertEquals(NotificationStatus.PENDING, result.getStatus());
        assertFalse(result.isRead());
        assertNotNull(result.getCreatedAt());
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void getNotificationsByStatus_ShouldReturnMatchingNotifications() {
        // Arrange
        List<Notification> sentNotifications = Collections.singletonList(
                notification.toBuilder().status(NotificationStatus.SENT).build());
        
        when(notificationRepository.findByUserIdAndStatusOrderByCreatedAtDesc("user123", NotificationStatus.SENT))
                .thenReturn(sentNotifications);

        // Act
        List<Notification> result = notificationService.getNotificationsByStatus("user123", NotificationStatus.SENT);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(NotificationStatus.SENT, result.get(0).getStatus());
        verify(notificationRepository).findByUserIdAndStatusOrderByCreatedAtDesc("user123", NotificationStatus.SENT);
    }
} 