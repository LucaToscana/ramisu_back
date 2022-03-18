package com.m2i.warhammermarket.service;

import com.m2i.warhammermarket.entity.DAO.NotificationDAO;
import com.m2i.warhammermarket.model.Message;

public interface NotificationService {

/*NOTIFICATIONS*/
    void sendOrderStatusNotification(long idOrder,  String email,String messageOrder);

	void sendAllUserNotifications(String mail);

	Message sendPrivateNotificationForNewMessange(String receiverName, String senderName);

	Message sendCustomPrivateNotification(String email, String message);

	void saveNotificationOrder(long idOrder, String email, String date, String message) ;
	
	void deleteNotification(String date, String mail);

	NotificationDAO saveNotification(String email, String date, String message);

	void sendMessage(Message message);


}