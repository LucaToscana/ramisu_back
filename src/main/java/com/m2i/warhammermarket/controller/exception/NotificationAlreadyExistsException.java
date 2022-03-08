package com.m2i.warhammermarket.controller.exception;

public class NotificationAlreadyExistsException extends RuntimeException {

    public NotificationAlreadyExistsException() {
        super("La Notification existe déjà");
    }
}
