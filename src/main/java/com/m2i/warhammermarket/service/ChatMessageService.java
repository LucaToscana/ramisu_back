package com.m2i.warhammermarket.service;

import com.m2i.warhammermarket.entity.DAO.ChatMessageDAO;
import com.m2i.warhammermarket.entity.DAO.NotificationDAO;
import com.m2i.warhammermarket.model.Message;

public interface ChatMessageService {

   ChatMessageDAO saveChatMessage(Message message) ;
	 void sendAllUserChats(String mail) ;
	 void sendAllChats(String username);


}