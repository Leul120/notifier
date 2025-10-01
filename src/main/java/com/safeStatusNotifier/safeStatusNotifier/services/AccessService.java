package com.safeStatusNotifier.safeStatusNotifier.services;

import com.safeStatusNotifier.safeStatusNotifier.entity.AccessRelationship;
import com.safeStatusNotifier.safeStatusNotifier.entity.User;
import com.safeStatusNotifier.safeStatusNotifier.requests.AccessResponse;
import com.safeStatusNotifier.safeStatusNotifier.requests.GrantAccessRequest;

import java.util.List;
import java.util.UUID;

public interface AccessService {
    AccessRelationship denyAccessRequest(UUID requestId);
    AccessRelationship approveAccessRequest(UUID requestId);
    AccessRelationship grantAccess(GrantAccessRequest request);
    List<AccessRelationship> getAccessRequests();
    List<User> getMonitoredUsers();
    List<User> getUsersMonitoringMe();
    AccessRelationship revokeAccess(UUID userId);
}
