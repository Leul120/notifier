package com.safeStatusNotifier.safeStatusNotifier.controllers;


import com.safeStatusNotifier.safeStatusNotifier.entity.User;
import com.safeStatusNotifier.safeStatusNotifier.requests.DeviceStatusDto;
import com.safeStatusNotifier.safeStatusNotifier.requests.DeviceStatusUpdateRequest;
import com.safeStatusNotifier.safeStatusNotifier.requests.UserDto;
import com.safeStatusNotifier.safeStatusNotifier.services.AccessService;
import com.safeStatusNotifier.safeStatusNotifier.services.DeviceStatusService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

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



    @MessageMapping("/chat.privateMessage")
    @Transactional
    public void sendDeviceStatus(DeviceStatusUpdateRequest status, StompHeaderAccessor headerAccessor) {
//        String sender=(String) headerAccessor.getSessionAttributes().get("username");
        System.out.println("sender"+status);
////        message.setSender(sender);


        // Send message to the specific user
        DeviceStatusDto dto=deviceStatusService.updateDeviceStatus(status);
        List<UserDto> users=accessService.getUsersMonitoringMe();
        for (UserDto user:users) {
            messagingTemplate.convertAndSendToUser(
                    user.getId(), "/queue/statuses", dto);
        }
    }



}