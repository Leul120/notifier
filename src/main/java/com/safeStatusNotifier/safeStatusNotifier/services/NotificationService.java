package com.safeStatusNotifier.safeStatusNotifier.services;

import com.safeStatusNotifier.safeStatusNotifier.entity.StatusNotification;
import com.safeStatusNotifier.safeStatusNotifier.entity.User;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    void createNotification(User user, String title, String message, StatusNotification.NotificationType type);
    List<StatusNotification> getNotificationsForUser(UUID userId);
    List<StatusNotification> getNotificationsForCurrentUser();
}
