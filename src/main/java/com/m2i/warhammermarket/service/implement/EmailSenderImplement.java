package com.m2i.warhammermarket.service.implement;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.m2i.warhammermarket.model.Mail;
import com.m2i.warhammermarket.service.EmailSenderService;

/**
 * Class used to send emails to users 
 */

@Service
public class EmailSenderImplement implements EmailSenderService {

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	/**
	 * Creates and sends an email
	 *
	 * @param mail the mail that will be sent
	 * @throws MessagingException
	 * @throws IOException
	 * @author Cecile
	 */
	@Async
	public void sendEmail(Mail mail) throws MessagingException, IOException {

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper (
				message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		String html = getHtmlContent(mail);

		messageHelper.setTo(mail.getTo());
		messageHelper.setFrom(mail.getFrom());
		System.out.println(mail.getFrom());
		messageHelper.setSubject(mail.getSubject());
		System.out.println(mail.getSubject());

		messageHelper.setText(html, true);

		emailSender.send(message);
	}


	/**
	 * Creates and insert HTML inside an email and returns it
	 *
	 * @param mail the email that will get the content
	 * @return String : the template with the HTML content
	 * @author Cecile
	 */
	private String getHtmlContent(Mail mail) {
		Context context = new Context();
		context.setVariables(mail.getHtmlTemplate().getProps());
		return templateEngine.process(mail.getHtmlTemplate().getTemplate(), context);
	}

}
