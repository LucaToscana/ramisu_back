package com.m2i.warhammermarket.service.implement;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import model.Mail;

/**
 * Class used to send emails to users 
 */
public class EmailSenderImplement {

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	public void sendEmail(Mail mail) throws MessagingException, IOException {

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper (
				message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		String html = getHtmlContent(mail);

		messageHelper.setTo(mail.getTo());
		messageHelper.setFrom(mail.getFrom());
		messageHelper.setSubject(mail.getSubject());
		messageHelper.setText(html, true);

		emailSender.send(message);
	}


	private String getHtmlContent(Mail mail) {
		Context context = new Context();
		context.setVariables(mail.getHtmlTemplate().getProps());
		return templateEngine.process(mail.getHtmlTemplate().getTemplate(), context);
	}

}
