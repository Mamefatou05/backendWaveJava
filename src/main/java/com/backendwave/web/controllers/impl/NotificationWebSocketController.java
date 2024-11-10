package com.backendwave.web.controllers.impl;

import com.backendwave.data.entities.Notification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

@Controller
public class NotificationWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(Long utilisateurId, Notification notification) {
        String destination = "/topic/notifications/" + utilisateurId;
        messagingTemplate.convertAndSend(destination, notification);
    }
}
