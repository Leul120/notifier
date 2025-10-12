package com.safeStatusNotifier.safeStatusNotifier.services;

import com.safeStatusNotifier.safeStatusNotifier.entity.DeviceStatus;
import com.safeStatusNotifier.safeStatusNotifier.entity.User;
import com.safeStatusNotifier.safeStatusNotifier.requests.DeviceStatusDto;
import com.safeStatusNotifier.safeStatusNotifier.requests.DeviceStatusUpdateRequest;
import com.safeStatusNotifier.safeStatusNotifier.requests.WebSocketResponse;

import java.util.UUID;

public interface DeviceStatusService {
    DeviceStatusDto getUserDeviceStatus(UUID userId);
    WebSocketResponse updateDeviceStatus(DeviceStatusUpdateRequest request);
}
