package com.m2i.warhammermarket.service;

import java.io.IOException;

import javax.mail.MessagingException;

import com.m2i.warhammermarket.model.Mail;

public interface EmailSenderService {
	
	void sendEmail(Mail mail) throws MessagingException, IOException;

}
