package com.m2i.warhammermarket.service;




public interface NotificationService {


    void sendOrderStatusNotification(long idOrder,  String email,String messageOrder);

	void sendAllUserNotifications(String mail);

	void sendPrivateNotificationForNewMessange(String receiverName, String senderName);

	void sendCustomPrivateNotification(String email, String message);

	void saveNotificationOrder(long idOrder, String email, String date, String message) ;
	
	void deleteNotification(String date, String mail);


}