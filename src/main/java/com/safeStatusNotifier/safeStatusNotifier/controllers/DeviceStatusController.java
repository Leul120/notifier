package com.safeStatusNotifier.safeStatusNotifier.controllers;

import com.safeStatusNotifier.safeStatusNotifier.requests.DeviceStatusDto;
import com.safeStatusNotifier.safeStatusNotifier.requests.DeviceStatusUpdateRequest;
import com.safeStatusNotifier.safeStatusNotifier.services.DeviceStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class DeviceStatusController {

    private final DeviceStatusService deviceStatusService;

    public DeviceStatusController(DeviceStatusService deviceStatusService) {
        this.deviceStatusService = deviceStatusService;
    }

    @GetMapping("/users/{userId}/device-status")
    public ResponseEntity<DeviceStatusDto> getUserDeviceStatus(@PathVariable UUID userId) {
        return ResponseEntity.ok(deviceStatusService.getUserDeviceStatus(userId));
    }

    @PostMapping("/device-status")
    public ResponseEntity<DeviceStatusDto> updateDeviceStatus(@RequestBody DeviceStatusUpdateRequest request) {
        return ResponseEntity.ok(deviceStatusService.updateDeviceStatus(request));
    }
}