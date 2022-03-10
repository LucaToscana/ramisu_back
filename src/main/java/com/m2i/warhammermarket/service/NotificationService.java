package com.m2i.warhammermarket.service;

import com.m2i.warhammermarket.model.Message;

public interface NotificationService {


    void sendOrderStatusNotification(long idOrder,  String email,String messageOrder);

	void sendAllUserNotifications(String mail);

	void sendPrivateNotificationForNewMessange(String receiverName, String senderName);

	Message sendCustomPrivateNotification(String email, String message);

	void saveNotificationOrder(long idOrder, String email, String date, String message) ;
	
	void deleteNotification(String date, String mail);

	void saveNotification(String email, String date, String message);


}