package com.m2i.warhammermarket.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.m2i.warhammermarket.model.Message;
import com.m2i.warhammermarket.service.NotificationService;
import com.m2i.warhammermarket.service.WSService;
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChatController {
	  @Autowired
	    private WSService service;
	  @Autowired
	    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message){
    	
    	service.notifyFrontend("benvenuto");

        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        notificationService.sendPrivateNotification(message.getReceiverName());
        System.out.println(message.toString());
        return message;
    }
}
