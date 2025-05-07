package com.safeStatusNotifier.safeStatusNotifier.services.impl;

import com.safeStatusNotifier.safeStatusNotifier.entity.AccessRelationship;
import com.safeStatusNotifier.safeStatusNotifier.entity.StatusNotification;
import com.safeStatusNotifier.safeStatusNotifier.entity.User;
import com.safeStatusNotifier.safeStatusNotifier.repositories.AccessRelationshipRepository;
import com.safeStatusNotifier.safeStatusNotifier.repositories.UserRepository;
import com.safeStatusNotifier.safeStatusNotifier.requests.AccessRelationshipDto;
import com.safeStatusNotifier.safeStatusNotifier.requests.AccessResponse;
import com.safeStatusNotifier.safeStatusNotifier.requests.GrantAccessRequest;
import com.safeStatusNotifier.safeStatusNotifier.requests.UserDto;
import com.safeStatusNotifier.safeStatusNotifier.services.AccessService;
import com.safeStatusNotifier.safeStatusNotifier.services.NotificationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccessServiceImpl implements AccessService {

    private final AccessRelationshipRepository accessRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public AccessServiceImpl(AccessRelationshipRepository accessRepository,
                         UserRepository userRepository,
                         NotificationService notificationService) {
        this.accessRepository = accessRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }
    @Override
    public List<UserDto> getUsersMonitoringMe() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<AccessRelationship> relationships = accessRepository.findByTargetAndStatus(
                currentUser, AccessRelationship.AccessStatus.APPROVED);

        return relationships.stream()
                .map(relationship -> mapUserToDto(relationship.getRequester()))
                .collect(Collectors.toList());
    }
    @Override
    public List<UserDto> getMonitoredUsers() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<AccessRelationship> relationships = accessRepository.findByRequesterAndStatus(
                currentUser, AccessRelationship.AccessStatus.APPROVED);

        return relationships.stream()
                .map(relationship -> mapUserToDto(relationship.getTarget()))
                .collect(Collectors.toList());
    }
    @Override
    public List<AccessRelationshipDto> getAccessRequests() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<AccessRelationship> relationships = accessRepository.findByTarget(currentUser);

        return relationships.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    public AccessResponse grantAccess(GrantAccessRequest request) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        User targetUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        // Check if relationship already exists
        accessRepository.findByRequesterAndTarget(currentUser, targetUser)
                .ifPresent(relationship -> {
                    throw new RuntimeException("Access relationship already exists");
                });

        AccessRelationship relationship = new AccessRelationship();
        relationship.setRequester(currentUser);
        relationship.setTarget(targetUser);
        relationship.setStatus(AccessRelationship.AccessStatus.PENDING);
        relationship.setCreatedAt(LocalDateTime.now());

        AccessRelationship savedRelationship = accessRepository.save(relationship);

        // Create notification for target user
        notificationService.createNotification(
                targetUser,
                "Access Request",
                currentUser.getName() + " has requested to monitor your device status",
                StatusNotification.NotificationType.INFO
        );

        return new AccessResponse(
                true,
                "Access request sent successfully",
                mapToDto(savedRelationship)
        );
    }
    @Override
    public AccessResponse approveAccessRequest(UUID requestId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AccessRelationship relationship = accessRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Access request not found"));

        // Verify that the current user is the target of the request
        if (!relationship.getTarget().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to approve this request");
        }

        relationship.setStatus(AccessRelationship.AccessStatus.APPROVED);
        AccessRelationship savedRelationship = accessRepository.save(relationship);

        // Create notification for requester
        notificationService.createNotification(
                relationship.getRequester(),
                "Access Request Approved",
                currentUser.getName() + " has approved your access request",
                StatusNotification.NotificationType.SUCCESS
        );

        return new AccessResponse(
                true,
                "Access request approved successfully",
                mapToDto(savedRelationship)
        );
    }
    @Override
    public AccessResponse denyAccessRequest(UUID requestId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AccessRelationship relationship = accessRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Access request not found"));

        // Verify that the current user is the target of the request
        if (!relationship.getTarget().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to deny this request");
        }

        relationship.setStatus(AccessRelationship.AccessStatus.DENIED);
        AccessRelationship savedRelationship = accessRepository.save(relationship);

        // Create notification for requester
        notificationService.createNotification(
                relationship.getRequester(),
                "Access Request Denied",
                currentUser.getName() + " has denied your access request",
                StatusNotification.NotificationType.WARNING
        );

        return new AccessResponse(
                true,
                "Access request denied successfully",
                mapToDto(savedRelationship)
        );
    }
    @Override
    public AccessResponse revokeAccess(UUID userId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        AccessRelationship relationship = accessRepository.findByRequesterAndTarget(targetUser, currentUser)
                .orElseThrow(() -> new RuntimeException("Access relationship not found"));

        accessRepository.delete(relationship);

        // Create notification for requester
        notificationService.createNotification(
                targetUser,
                "Access Revoked",
                currentUser.getName() + " has revoked your access",
                StatusNotification.NotificationType.WARNING
        );

        return new AccessResponse(
                true,
                "Access revoked successfully",
                null
        );
    }

    private UserDto mapUserToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId().toString());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        return dto;
    }

    private AccessRelationshipDto mapToDto(AccessRelationship relationship) {
        AccessRelationshipDto dto = new AccessRelationshipDto();
        dto.setId(relationship.getId().toString());
        dto.setRequesterId(relationship.getRequester().getId().toString());
        dto.setRequesterName(relationship.getRequester().getName());
        dto.setRequesterEmail(relationship.getRequester().getEmail());
        dto.setTargetId(relationship.getTarget().getId().toString());
        dto.setTargetName(relationship.getTarget().getName());
        dto.setTargetEmail(relationship.getTarget().getEmail());
        dto.setStatus(relationship.getStatus());
        dto.setCreatedAt(relationship.getCreatedAt().format(formatter));
        return dto;
    }
}