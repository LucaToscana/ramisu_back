package com.m2i.warhammermarket.controller.exception;

public class UserNotFoundException extends RuntimeException {
	
	public UserNotFoundException() {
		super("Utilisateur non trouvé");
	}
}
