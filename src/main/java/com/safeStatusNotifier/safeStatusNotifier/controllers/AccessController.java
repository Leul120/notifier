package com.safeStatusNotifier.safeStatusNotifier.controllers;


import com.safeStatusNotifier.safeStatusNotifier.entity.AccessRelationship;
import com.safeStatusNotifier.safeStatusNotifier.entity.User;
import com.safeStatusNotifier.safeStatusNotifier.requests.AccessRelationshipDto;
import com.safeStatusNotifier.safeStatusNotifier.requests.AccessResponse;
import com.safeStatusNotifier.safeStatusNotifier.requests.GrantAccessRequest;
import com.safeStatusNotifier.safeStatusNotifier.requests.UserDto;
import com.safeStatusNotifier.safeStatusNotifier.services.AccessService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

//@RestController
//@RequestMapping("/api/access")
@Controller
public class AccessController {

    private final AccessService accessService;

    public AccessController(AccessService accessService) {
        this.accessService = accessService;
    }

    @QueryMapping
    public List<User> usersMonitoringMe() {
        return accessService.getUsersMonitoringMe();
    }

    @QueryMapping
    public List<User> monitoredUsers() {
        return accessService.getMonitoredUsers();
    }

    @QueryMapping
    public List<AccessRelationship> accessRequests() {
        return accessService.getAccessRequests();
    }

    @MutationMapping
    public AccessRelationship grantAccess(@Argument GrantAccessRequest input) {
        System.out.println("input "+input);

        GrantAccessRequest request = new GrantAccessRequest();
        request.setEmail(input.getEmail());
        System.out.println("req "+request);
        return accessService.grantAccess(request);
    }

    @MutationMapping
    public AccessRelationship approveAccessRequest(@Argument String id) {
        return accessService.approveAccessRequest(UUID.fromString(id));
    }

    @MutationMapping
    public AccessRelationship denyAccessRequest(@Argument String id) {
        return accessService.denyAccessRequest(UUID.fromString(id));
    }

    @MutationMapping
    public Boolean revokeAccess(@Argument String id) {
        accessService.revokeAccess(UUID.fromString(id));
        return true;
    }
}
