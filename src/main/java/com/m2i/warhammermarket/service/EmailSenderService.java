package com.m2i.warhammermarket.service;

import model.Mail;

public interface EmailSenderService {
	
	void sendMail(Mail mail);
	
	String getHtmlContent(Mail mail);

}
