package com.safeStatusNotifier.safeStatusNotifier.controllers;

import com.safeStatusNotifier.safeStatusNotifier.entity.StatusNotification;
import com.safeStatusNotifier.safeStatusNotifier.entity.User;
import com.safeStatusNotifier.safeStatusNotifier.repositories.UserRepository;
import com.safeStatusNotifier.safeStatusNotifier.requests.StatusNotificationDto;
import com.safeStatusNotifier.safeStatusNotifier.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

//@RestController
//@RequestMapping("/api")
@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

//    public NotificationController(NotificationService notificationService,UserRepository userRepository) {
//        this.notificationService = notificationService;
//        this.userRepository=userRepository;
//    }

    @QueryMapping
    public List<StatusNotification> getNotifications() {
        return notificationService.getNotificationsForCurrentUser();
    }

    @QueryMapping
    public List<StatusNotification> getUserNotifications(@Argument String userId) {
        return notificationService.getNotificationsForUser(UUID.fromString(userId));
    }
    @MutationMapping
    public StatusNotification createNotification(@Argument StatusNotificationDto input) {
        User user = userRepository.findById(UUID.fromString(input.getUserId()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        notificationService.createNotification(
                user,
                input.getTitle(),
                input.getMessage(),
                input.getType()
        );
        return notificationService.getNotificationsForUser(user.getId()).get(0);
    }
}
