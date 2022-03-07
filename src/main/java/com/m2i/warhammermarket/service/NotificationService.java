package com.m2i.warhammermarket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.m2i.warhammermarket.model.Message;
import com.m2i.warhammermarket.model.ResponseMessage;
import com.m2i.warhammermarket.model.StatusMessage;

@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendGlobalNotification() {
        ResponseMessage message = new ResponseMessage("Global Notification");

        messagingTemplate.convertAndSend("/topic/global-notifications", message);
    }

    public void sendPrivateNotification(final String email) {
        Message message = new Message("Private Notification", "warhammermarket", email,StatusMessage.NOTIFICATION);
      messagingTemplate.convertAndSendToUser(email,"/notifications/private-messages",message);

    }
    
    
    public void sendCustomPrivateNotification(final String email, String messageNotification) {
    	/*(String senderName, String receiverName, String message, StatusMessage status) */
        Message message = new Message("warhammermarket",email, messageNotification,StatusMessage.NOTIFICATION);
      messagingTemplate.convertAndSendToUser(email,"/notifications/private-messages",message);

    }
    
    
    
    public void sendPrivateNotificationForNewMessange(final String email,final String emailSender) {
        Message message = new Message();
        message.setReceiverName(email);
        message.setSenderName(emailSender);
        message.setStatus(StatusMessage.NOTIFICATION);
        message.setMessage(emailSender + "   vous a envoyé un nouveau message");
        messagingTemplate.convertAndSendToUser(email,"/notifications/private-messages",message);

    }

	public void sendOrderStatusNotification(long idOrder,final String email) {
		 Message message = new Message();
	        message.setReceiverName(email);
	        message.setSenderName( "warhammermarket");
	        message.setStatus(StatusMessage.NOTIFICATION);
	        message.setMessage("Le statut d'une commande vient de changer !");
	        message.setIdorder(idOrder);
	        messagingTemplate.convertAndSendToUser(email,"/notifications/private-messages",message);		
	}
    
}