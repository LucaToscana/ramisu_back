package com.m2i.warhammermarket.service;




public interface NotificationService {


    void sendOrderStatusNotification(long idOrder,  String email,String messageOrder);

	void sendAllUserNotification(String mail);

	void deleteNotificationStatusOrder(String date, String username);

	void sendPrivateNotificationForNewMessange(String receiverName, String senderName);

	void sendCustomPrivateNotification(String email, String message);

	

}