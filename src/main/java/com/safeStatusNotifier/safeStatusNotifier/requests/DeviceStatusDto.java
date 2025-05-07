package com.safeStatusNotifier.safeStatusNotifier.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceStatusDto {
    private String userId;
    private boolean isOnline;
    private int batteryLevel;
    private String networkType;
    private String lastSeen;
}
