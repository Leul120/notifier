package com.safeStatusNotifier.safeStatusNotifier.requests;



import com.safeStatusNotifier.safeStatusNotifier.entity.StatusNotification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusNotificationDto {
    private String id;
    private String userId;
    private String title;
    private String message;
    private StatusNotification.NotificationType type;
    private String timestamp;
    private boolean isRead;
}