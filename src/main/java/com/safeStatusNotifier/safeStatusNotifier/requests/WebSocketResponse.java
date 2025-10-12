package com.safeStatusNotifier.safeStatusNotifier.requests;

import com.safeStatusNotifier.safeStatusNotifier.entity.DeviceStatus;
import com.safeStatusNotifier.safeStatusNotifier.entity.StatusNotification;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WebSocketResponse {
    private DeviceStatus deviceStatus;
    private List<StatusNotification> notifications;
}
