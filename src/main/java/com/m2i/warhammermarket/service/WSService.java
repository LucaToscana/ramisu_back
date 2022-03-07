package com.m2i.warhammermarket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.m2i.warhammermarket.model.ResponseMessage;

@Service
public class WSService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    @Autowired
    public WSService(SimpMessagingTemplate messagingTemplate, NotificationService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    public void notifyFrontend(final String message) {
        ResponseMessage response = new ResponseMessage(message);
        notificationService.sendGlobalNotification();

        messagingTemplate.convertAndSend("/notifications/messages", response);
    }

    public void notifyUser(final String mail, final String message) {
        notificationService.sendPrivateNotification(mail);
        messagingTemplate.convertAndSendToUser("lucatscn@gmail.com","/private",message);
    }
}