package com.safeStatusNotifier.safeStatusNotifier.controllers;


import com.safeStatusNotifier.safeStatusNotifier.requests.AccessRelationshipDto;
import com.safeStatusNotifier.safeStatusNotifier.requests.AccessResponse;
import com.safeStatusNotifier.safeStatusNotifier.requests.GrantAccessRequest;
import com.safeStatusNotifier.safeStatusNotifier.requests.UserDto;
import com.safeStatusNotifier.safeStatusNotifier.services.AccessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/access")
public class AccessController {

    private final AccessService accessService;

    public AccessController(AccessService accessService) {
        this.accessService = accessService;
    }

    @GetMapping("/monitoring-me")
    public ResponseEntity<List<UserDto>> getUsersMonitoringMe() {
        return ResponseEntity.ok(accessService.getUsersMonitoringMe());
    }

    @GetMapping("/monitored-by-me")
    public ResponseEntity<List<UserDto>> getMonitoredUsers() {
        return ResponseEntity.ok(accessService.getMonitoredUsers());
    }

    @GetMapping("/requests")
    public ResponseEntity<List<AccessRelationshipDto>> getAccessRequests() {
        return ResponseEntity.ok(accessService.getAccessRequests());
    }

    @PostMapping("/grant")
    public ResponseEntity<AccessResponse> grantAccess(@RequestBody GrantAccessRequest request) {
        return ResponseEntity.ok(accessService.grantAccess(request));
    }

    @PostMapping("/requests/{requestId}/approve")
    public ResponseEntity<AccessResponse> approveAccessRequest(@PathVariable UUID requestId) {
        return ResponseEntity.ok(accessService.approveAccessRequest(requestId));
    }

    @PostMapping("/requests/{requestId}/deny")
    public ResponseEntity<AccessResponse> denyAccessRequest(@PathVariable UUID requestId) {
        return ResponseEntity.ok(accessService.denyAccessRequest(requestId));
    }

    @DeleteMapping("/revoke/{userId}")
    public ResponseEntity<AccessResponse> revokeAccess(@PathVariable UUID userId) {
        return ResponseEntity.ok(accessService.revokeAccess(userId));
    }
}
