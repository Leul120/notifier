package com.safeStatusNotifier.safeStatusNotifier.services;

import com.safeStatusNotifier.safeStatusNotifier.entity.DeviceStatus;
import com.safeStatusNotifier.safeStatusNotifier.entity.User;
import com.safeStatusNotifier.safeStatusNotifier.requests.DeviceStatusDto;
import com.safeStatusNotifier.safeStatusNotifier.requests.DeviceStatusUpdateRequest;

import java.util.UUID;

public interface DeviceStatusService {
    DeviceStatus getUserDeviceStatus(UUID userId);
    DeviceStatus updateDeviceStatus(DeviceStatusUpdateRequest request);
}
