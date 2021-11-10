package com.m2i.warhammermarket.controller.exception;

public class UserMailAlreadyExistException extends RuntimeException {

    public UserMailAlreadyExistException() {
        super("L'email existe déjà");
    }
}
