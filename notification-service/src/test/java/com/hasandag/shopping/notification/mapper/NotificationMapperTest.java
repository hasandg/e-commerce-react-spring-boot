package com.hasandag.ecommerce.notification.mapper;

import com.hasandag.ecommerce.notification.dto.NotificationRequest;
import com.hasandag.ecommerce.notification.dto.NotificationResponse;
import com.hasandag.ecommerce.notification.model.Notification;
import com.hasandag.ecommerce.notification.model.NotificationStatus;
import com.hasandag.ecommerce.notification.model.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationMapperTest {

    private NotificationMapper mapper;
    private Notification notification;
    private NotificationRequest request;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        mapper = new NotificationMapper();
        now = LocalDateTime.now();
        
        // Set up a sample notification
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
                .updatedAt(now.plusMinutes(5))
                .sentAt(now.plusMinutes(10))
                .readAt(null)
                .build();
        
        // Set up a sample request
        request = NotificationRequest.builder()
                .userId("user123")
                .subject("Test Subject")
                .message("Test Message")
                .type(NotificationType.IN_APP)
                .relatedEntityId("TEST-123")
                .relatedEntityType("TEST")
                .build();
    }

    @Test
    void toEntity_ShouldMapRequestToNotification() {
        // Act
        Notification result = mapper.toEntity(request);
        
        // Assert
        assertNotNull(result);
        assertNull(result.getId()); // ID should not be set
        assertEquals("user123", result.getUserId());
        assertEquals("Test Subject", result.getSubject());
        assertEquals("Test Message", result.getMessage());
        assertEquals(NotificationType.IN_APP, result.getType());
        assertEquals(NotificationStatus.PENDING, result.getStatus());
        assertEquals("TEST-123", result.getRelatedEntityId());
        assertEquals("TEST", result.getRelatedEntityType());
        assertFalse(result.isRead());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void toResponse_ShouldMapNotificationToResponse() {
        // Act
        NotificationResponse response = mapper.toResponse(notification);
        
        // Assert
        assertNotNull(response);
        assertEquals("1", response.getId());
        assertEquals("user123", response.getUserId());
        assertEquals("Test Subject", response.getSubject());
        assertEquals("Test Message", response.getMessage());
        assertEquals(NotificationType.IN_APP, response.getType());
        assertEquals(NotificationStatus.PENDING, response.getStatus());
        assertEquals("TEST-123", response.getRelatedEntityId());
        assertEquals("TEST", response.getRelatedEntityType());
        assertFalse(response.isRead());
        assertEquals(now, response.getCreatedAt());
        assertEquals(now.plusMinutes(5), response.getUpdatedAt());
        assertEquals(now.plusMinutes(10), response.getSentAt());
        assertNull(response.getReadAt());
    }

    @Test
    void toResponseList_ShouldMapListOfNotificationsToListOfResponses() {
        // Arrange
        Notification notification2 = notification.toBuilder()
                .id("2")
                .subject("Another Subject")
                .message("Another Message")
                .build();
        
        List<Notification> notifications = Arrays.asList(notification, notification2);
        
        // Act
        List<NotificationResponse> responses = mapper.toResponseList(notifications);
        
        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        
        // First response assertions
        assertEquals("1", responses.get(0).getId());
        assertEquals("Test Subject", responses.get(0).getSubject());
        
        // Second response assertions
        assertEquals("2", responses.get(1).getId());
        assertEquals("Another Subject", responses.get(1).getSubject());
    }

    @Test
    void toResponse_WhenNotificationIsNull_ShouldReturnNull() {
        // Act
        NotificationResponse response = mapper.toResponse(null);
        
        // Assert
        assertNull(response);
    }

    @Test
    void toResponseList_WhenNotificationsIsNull_ShouldReturnEmptyList() {
        // Act
        List<NotificationResponse> responses = mapper.toResponseList(null);
        
        // Assert
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }
} 