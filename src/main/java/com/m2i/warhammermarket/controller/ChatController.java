package com.m2i.warhammermarket.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m2i.warhammermarket.model.Message;
import com.m2i.warhammermarket.service.ChatMessageService;
import com.m2i.warhammermarket.service.NotificationService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChatController {
	
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private ChatMessageService chatService;
	

	@MessageMapping("/message")
	@SendTo("/chatroom/public")
	public Message receiveMessage(@Payload Message message) {

		return message;
	}

	@MessageMapping("/private-message")
	public Message recMessage(@Payload Message message) {
		
		notificationService.sendMessage(message);
		
		return message;
	}
	
	
	
	@GetMapping("/api/user/all-chats")
	public List<Message> getMessage() {
		List<Message> messages = new ArrayList<Message>();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userDetails!=null)
        {	return	chatService.sendAllChatsList(); }
	
		
		return null;
	}
}
