package com.safeStatusNotifier.safeStatusNotifier.controllers;


import com.safeStatusNotifier.safeStatusNotifier.entity.User;
import com.safeStatusNotifier.safeStatusNotifier.repositories.UserRepository;
import com.safeStatusNotifier.safeStatusNotifier.requests.DeviceStatusDto;
import com.safeStatusNotifier.safeStatusNotifier.requests.DeviceStatusUpdateRequest;
import com.safeStatusNotifier.safeStatusNotifier.requests.UserDto;
import com.safeStatusNotifier.safeStatusNotifier.requests.WebSocketResponse;
import com.safeStatusNotifier.safeStatusNotifier.services.AccessService;
import com.safeStatusNotifier.safeStatusNotifier.services.DeviceStatusService;
import com.safeStatusNotifier.safeStatusNotifier.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
//@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
public class WebSocketController {
    Logger log= LoggerFactory.getLogger(WebSocketController.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final AccessService accessService;
    private final DeviceStatusService deviceStatusService;
    private final UserRepository userRepository;



    @MessageMapping("/chat.privateMessage")
    @Transactional
    public void sendDeviceStatus(DeviceStatusUpdateRequest status, Principal principal) {
        String senderEmail = principal.getName();
        System.out.println("Sender: " + senderEmail);

        WebSocketResponse webSocketResponse = deviceStatusService.updateDeviceStatus(status, senderEmail);

        List<UserDto> users = accessService.getUsersMonitoringMe(senderEmail);
        System.out.println("Users to notify: " + users.size());

        for (UserDto user1: users) {
            String destination = "/user/" + user1.getId() + "/queue/status";
            System.out.println("Sending to: " + destination);
            System.out.println("Message: " + webSocketResponse);

            messagingTemplate.convertAndSendToUser(
                    user1.getId(), "/queue/status", webSocketResponse);
        }
    }



}