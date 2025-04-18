package com.hasandag.ecommerce.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hasandag.ecommerce.notification.config.TestConfig;
import com.hasandag.ecommerce.notification.dto.NotificationRequest;
import com.hasandag.ecommerce.notification.dto.NotificationResponse;
import com.hasandag.ecommerce.notification.model.Notification;
import com.hasandag.ecommerce.notification.model.NotificationStatus;
import com.hasandag.ecommerce.notification.model.NotificationType;
import com.hasandag.ecommerce.notification.service.NotificationService;
import com.hasandag.ecommerce.notification.mapper.NotificationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@Import(TestConfig.class)
@ActiveProfiles("test")
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private NotificationMapper notificationMapper;

    private Notification notification;
    private NotificationResponse notificationResponse;
    private NotificationRequest notificationRequest;

    @BeforeEach
    void setUp() {
        // Set up test data
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

        notificationResponse = NotificationResponse.builder()
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

        notificationRequest = NotificationRequest.builder()
                .userId("user123")
                .subject("Test Subject")
                .message("Test Message")
                .type(NotificationType.IN_APP)
                .relatedEntityId("TEST-123")
                .relatedEntityType("TEST")
                .build();
    }

    @Test
    void createNotification_ShouldReturnCreatedNotification() throws Exception {
        // Arrange
        when(notificationService.createNotification(
                anyString(), anyString(), anyString(), any(NotificationType.class), anyString(), anyString()))
                .thenReturn(notification);
        when(notificationMapper.toResponse(any(Notification.class))).thenReturn(notificationResponse);

        // Act & Assert
        mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.userId").value("user123"))
                .andExpect(jsonPath("$.subject").value("Test Subject"))
                .andExpect(jsonPath("$.message").value("Test Message"))
                .andExpect(jsonPath("$.type").value("IN_APP"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.read").value(false));

        // Verify
        verify(notificationService).createNotification(
                "user123", "Test Subject", "Test Message", NotificationType.IN_APP, "TEST-123", "TEST");
        verify(notificationMapper).toResponse(notification);
    }

    @Test
    void getUserNotifications_ShouldReturnPageOfNotifications() throws Exception {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Notification> notificationPage = new PageImpl<>(
                Collections.singletonList(notification), pageable, 1);
        Page<NotificationResponse> responsePage = new PageImpl<>(
                Collections.singletonList(notificationResponse), pageable, 1);

        when(notificationService.getUserNotifications(eq("user123"), any(Pageable.class)))
                .thenReturn(notificationPage);
        when(notificationMapper.toResponse(notification)).thenReturn(notificationResponse);

        // Act & Assert
        mockMvc.perform(get("/api/notifications")
                .header("X-User-ID", "user123")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("1"))
                .andExpect(jsonPath("$.content[0].userId").value("user123"))
                .andExpect(jsonPath("$.content[0].subject").value("Test Subject"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));

        // Verify
        verify(notificationService).getUserNotifications(eq("user123"), any(Pageable.class));
        verify(notificationMapper).toResponse(notification);
    }

    @Test
    void getUnreadNotifications_ShouldReturnUnreadNotifications() throws Exception {
        // Arrange
        List<Notification> unreadNotifications = Collections.singletonList(notification);
        List<NotificationResponse> unreadResponses = Collections.singletonList(notificationResponse);

        when(notificationService.getUnreadNotifications("user123")).thenReturn(unreadNotifications);
        when(notificationMapper.toResponseList(unreadNotifications)).thenReturn(unreadResponses);

        // Act & Assert
        mockMvc.perform(get("/api/notifications/unread")
                .header("X-User-ID", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].userId").value("user123"))
                .andExpect(jsonPath("$[0].subject").value("Test Subject"))
                .andExpect(jsonPath("$[0].read").value(false));

        // Verify
        verify(notificationService).getUnreadNotifications("user123");
        verify(notificationMapper).toResponseList(unreadNotifications);
    }

    @Test
    void getUnreadCount_ShouldReturnCount() throws Exception {
        // Arrange
        when(notificationService.getUnreadCount("user123")).thenReturn(5L);

        // Act & Assert
        mockMvc.perform(get("/api/notifications/count")
                .header("X-User-ID", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(5));

        // Verify
        verify(notificationService).getUnreadCount("user123");
    }

    @Test
    void markAsRead_ShouldReturnUpdatedNotification() throws Exception {
        // Arrange
        Notification readNotification = notification.toBuilder()
                .read(true)
                .readAt(LocalDateTime.now())
                .build();
        
        NotificationResponse readResponse = notificationResponse.toBuilder()
                .read(true)
                .readAt(readNotification.getReadAt())
                .build();

        when(notificationService.markAsRead("1")).thenReturn(readNotification);
        when(notificationMapper.toResponse(readNotification)).thenReturn(readResponse);

        // Act & Assert
        mockMvc.perform(put("/api/notifications/1/read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.userId").value("user123"))
                .andExpect(jsonPath("$.read").value(true))
                .andExpect(jsonPath("$.readAt").isNotEmpty());

        // Verify
        verify(notificationService).markAsRead("1");
        verify(notificationMapper).toResponse(readNotification);
    }

    @Test
    void markAllAsRead_ShouldReturnUpdatedNotifications() throws Exception {
        // Arrange
        Notification readNotification = notification.toBuilder()
                .read(true)
                .readAt(LocalDateTime.now())
                .build();
        
        List<Notification> readNotifications = Collections.singletonList(readNotification);
        
        NotificationResponse readResponse = notificationResponse.toBuilder()
                .read(true)
                .readAt(readNotification.getReadAt())
                .build();
        
        List<NotificationResponse> readResponses = Collections.singletonList(readResponse);

        when(notificationService.markAllAsRead("user123")).thenReturn(readNotifications);
        when(notificationMapper.toResponseList(readNotifications)).thenReturn(readResponses);

        // Act & Assert
        mockMvc.perform(put("/api/notifications/read-all")
                .header("X-User-ID", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].userId").value("user123"))
                .andExpect(jsonPath("$[0].read").value(true))
                .andExpect(jsonPath("$[0].readAt").isNotEmpty());

        // Verify
        verify(notificationService).markAllAsRead("user123");
        verify(notificationMapper).toResponseList(readNotifications);
    }

    @Test
    void deleteNotification_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(notificationService).deleteNotification("1");

        // Act & Assert
        mockMvc.perform(delete("/api/notifications/1"))
                .andExpect(status().isNoContent());

        // Verify
        verify(notificationService).deleteNotification("1");
    }

    @Test
    void getNotificationsByStatus_ShouldReturnMatchingNotifications() throws Exception {
        // Arrange
        List<Notification> sentNotifications = Collections.singletonList(
                notification.toBuilder().status(NotificationStatus.SENT).build());
        
        List<NotificationResponse> sentResponses = Collections.singletonList(
                notificationResponse.toBuilder().status(NotificationStatus.SENT).build());

        when(notificationService.getNotificationsByStatus("user123", NotificationStatus.SENT))
                .thenReturn(sentNotifications);
        when(notificationMapper.toResponseList(sentNotifications)).thenReturn(sentResponses);

        // Act & Assert
        mockMvc.perform(get("/api/notifications/status/SENT")
                .header("X-User-ID", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].userId").value("user123"))
                .andExpect(jsonPath("$[0].status").value("SENT"));

        // Verify
        verify(notificationService).getNotificationsByStatus("user123", NotificationStatus.SENT);
        verify(notificationMapper).toResponseList(sentNotifications);
    }
} 