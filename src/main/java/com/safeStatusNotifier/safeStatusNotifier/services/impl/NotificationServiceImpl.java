package com.safeStatusNotifier.safeStatusNotifier.services.impl;

import com.safeStatusNotifier.safeStatusNotifier.entity.StatusNotification;
import com.safeStatusNotifier.safeStatusNotifier.entity.User;
import com.safeStatusNotifier.safeStatusNotifier.repositories.StatusNotificationRepository;
import com.safeStatusNotifier.safeStatusNotifier.repositories.UserRepository;
import com.safeStatusNotifier.safeStatusNotifier.requests.StatusNotificationDto;
import com.safeStatusNotifier.safeStatusNotifier.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final StatusNotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public NotificationServiceImpl(StatusNotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }
    @Override
    public List<StatusNotificationDto> getNotificationsForCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<StatusNotification> notifications = notificationRepository.findByUserOrderByTimestampDesc(user);

        return notifications.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<StatusNotificationDto> getNotificationsForUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if current user has access to view this user's notifications
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        // This would need to be expanded with proper access control logic

        List<StatusNotification> notifications = notificationRepository.findByUserOrderByTimestampDesc(user);

        return notifications.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    public void createNotification(User user, String title, String message, StatusNotification.NotificationType type) {
        StatusNotification notification = new StatusNotification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setTimestamp(LocalDateTime.now());
        notification.setRead(false);

        notificationRepository.save(notification);
    }

    private StatusNotificationDto mapToDto(StatusNotification notification) {
        StatusNotificationDto dto = new StatusNotificationDto();
        dto.setId(notification.getId().toString());
        dto.setUserId(notification.getUser().getId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setTimestamp(notification.getTimestamp().format(formatter));
        dto.setRead(notification.isRead());
        return dto;
    }
}

