package com.safeStatusNotifier.safeStatusNotifier.controllers;

import com.safeStatusNotifier.safeStatusNotifier.entity.DeviceStatus;
import com.safeStatusNotifier.safeStatusNotifier.requests.DeviceStatusDto;
import com.safeStatusNotifier.safeStatusNotifier.requests.DeviceStatusUpdateRequest;
import com.safeStatusNotifier.safeStatusNotifier.services.DeviceStatusService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

//@RestController
//@RequestMapping("/api")
@Controller
public class DeviceStatusController {

    private final DeviceStatusService deviceStatusService;

    public DeviceStatusController(DeviceStatusService deviceStatusService) {
        this.deviceStatusService = deviceStatusService;
    }

    @QueryMapping
    public DeviceStatus getUserDeviceStatus(@Argument String userId) {
        return deviceStatusService.getUserDeviceStatus(UUID.fromString(userId));
    }


    @MutationMapping
    public DeviceStatus updateDeviceStatus(@Argument DeviceStatusUpdateRequest input) {
        return deviceStatusService.updateDeviceStatus(input);
    }
}