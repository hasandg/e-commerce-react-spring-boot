package com.hasandag.ecommerce.notification.controller;

import com.hasandag.ecommerce.notification.dto.NotificationRequest;
import com.hasandag.ecommerce.notification.dto.NotificationResponse;
import com.hasandag.ecommerce.notification.mapper.NotificationMapper;
import com.hasandag.ecommerce.notification.model.Notification;
import com.hasandag.ecommerce.notification.model.NotificationStatus;
import com.hasandag.ecommerce.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(@Valid @RequestBody NotificationRequest request) {
        Notification notification = notificationService.createNotification(
                request.getUserId(),
                request.getSubject(),
                request.getMessage(),
                request.getType(),
                request.getRelatedEntityId(),
                request.getRelatedEntityType()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationMapper.toResponse(notification));
    }

    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getUserNotifications(
            @RequestHeader("X-User-ID") String userId,
            Pageable pageable) {
        Page<Notification> notificationsPage = notificationService.getUserNotifications(userId, pageable);
        List<NotificationResponse> responseList = notificationsPage.getContent().stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new PageImpl<>(responseList, pageable, notificationsPage.getTotalElements()));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(
            @RequestHeader("X-User-ID") String userId) {
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notificationMapper.toResponseList(notifications));
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @RequestHeader("X-User-ID") String userId) {
        long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable String id) {
        Notification notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notificationMapper.toResponse(notification));
    }

    @PutMapping("/read-all")
    public ResponseEntity<List<NotificationResponse>> markAllAsRead(
            @RequestHeader("X-User-ID") String userId) {
        List<Notification> notifications = notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(notificationMapper.toResponseList(notifications));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotification(@PathVariable String id) {
        notificationService.deleteNotification(id);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByStatus(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable NotificationStatus status) {
        List<Notification> notifications = notificationService.getNotificationsByStatus(userId, status);
        return ResponseEntity.ok(notificationMapper.toResponseList(notifications));
    }
} 