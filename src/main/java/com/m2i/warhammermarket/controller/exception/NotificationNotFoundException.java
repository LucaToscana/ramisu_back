package com.m2i.warhammermarket.controller.exception;

public class NotificationNotFoundException extends RuntimeException {
	
	public NotificationNotFoundException() {
		super("Notification non trouvé");
	}
}
