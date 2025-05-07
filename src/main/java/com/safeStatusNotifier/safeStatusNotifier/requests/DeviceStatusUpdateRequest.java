package com.safeStatusNotifier.safeStatusNotifier.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceStatusUpdateRequest {
    private boolean isOnline;
    private int batteryLevel;
    private String networkType;
}
