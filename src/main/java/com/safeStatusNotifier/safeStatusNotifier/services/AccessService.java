package com.safeStatusNotifier.safeStatusNotifier.services;

import com.safeStatusNotifier.safeStatusNotifier.requests.AccessRelationshipDto;
import com.safeStatusNotifier.safeStatusNotifier.requests.AccessResponse;
import com.safeStatusNotifier.safeStatusNotifier.requests.GrantAccessRequest;
import com.safeStatusNotifier.safeStatusNotifier.requests.UserDto;

import java.util.List;
import java.util.UUID;

public interface AccessService {
    AccessResponse denyAccessRequest(String requestId);
    AccessResponse approveAccessRequest(String requestId);
    AccessResponse grantAccess(GrantAccessRequest request);
    List<AccessRelationshipDto> getAccessRequests();
    List<UserDto> getMonitoredUsers();
    List<UserDto> getUsersMonitoringMe();
    AccessResponse revokeAccess(UUID userId);
}
