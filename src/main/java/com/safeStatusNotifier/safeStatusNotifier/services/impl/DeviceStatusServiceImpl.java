package com.safeStatusNotifier.safeStatusNotifier.services.impl;


import com.safeStatusNotifier.safeStatusNotifier.entity.DeviceStatus;
import com.safeStatusNotifier.safeStatusNotifier.entity.StatusNotification;
import com.safeStatusNotifier.safeStatusNotifier.entity.User;
import com.safeStatusNotifier.safeStatusNotifier.repositories.DeviceStatusRepository;
import com.safeStatusNotifier.safeStatusNotifier.repositories.UserRepository;
import com.safeStatusNotifier.safeStatusNotifier.requests.DeviceStatusDto;
import com.safeStatusNotifier.safeStatusNotifier.requests.DeviceStatusUpdateRequest;
import com.safeStatusNotifier.safeStatusNotifier.services.DeviceStatusService;
import com.safeStatusNotifier.safeStatusNotifier.services.NotificationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class DeviceStatusServiceImpl implements DeviceStatusService {

    private final DeviceStatusRepository deviceStatusRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public DeviceStatusServiceImpl(DeviceStatusRepository deviceStatusRepository,
                               UserRepository userRepository,
                               NotificationService notificationService) {
        this.deviceStatusRepository = deviceStatusRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }
    @Override
    public DeviceStatus getUserDeviceStatus(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return deviceStatusRepository.findTopByUserOrderByLastSeenDesc(user)
                .orElseThrow(() -> new RuntimeException("Device status not found"));


    }
    @Override
    public DeviceStatus updateDeviceStatus(DeviceStatusUpdateRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DeviceStatus status = new DeviceStatus();
        status.setUser(user);
        status.setOnline(request.isOnline());
        status.setBatteryLevel(request.getBatteryLevel());
        status.setNetworkType(request.getNetworkType());
        status.setLastSeen(LocalDateTime.now());

        DeviceStatus savedStatus = deviceStatusRepository.save(status);

        // Create notifications based on device status
        createStatusNotifications(user, savedStatus);

        return savedStatus;
    }

    private void createStatusNotifications(User user, DeviceStatus status) {
        // Battery low notification
        if (status.getBatteryLevel() <= 20) {
            notificationService.createNotification(
                    user,
                    "Low Battery",
                    "Your device battery is at " + status.getBatteryLevel() + "%",
                    StatusNotification.NotificationType.WARNING
            );
        }

        // Network change notification
        if (status.getNetworkType().equals("Mobile Data")) {
            notificationService.createNotification(
                    user,
                    "Network Change",
                    "Your device is now using mobile data",
                    StatusNotification.NotificationType.INFO
            );
        }
    }

//    private DeviceStatusDto mapToDto(DeviceStatus status) {
//        DeviceStatusDto dto = new DeviceStatusDto();
//        dto.setUserId(status.getUser().getId().toString());
//        dto.setOnline(status.isOnline());
//        dto.setBatteryLevel(status.getBatteryLevel());
//        dto.setNetworkType(status.getNetworkType());
//        dto.setLastSeen(status.getLastSeen().format(formatter));
//        return dto;
//    }
}
