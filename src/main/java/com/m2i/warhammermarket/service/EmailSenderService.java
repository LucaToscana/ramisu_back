package com.m2i.warhammermarket.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import com.m2i.warhammermarket.configuration.ApplicationConstants;
import com.m2i.warhammermarket.entity.wrapper.UserMessage;
import com.m2i.warhammermarket.model.Mail;

public interface EmailSenderService {

	/*
	* 		 compose the password change email
	* 	@param String firstName
	*  	@param lastName
	* 	@param String passwordToken
	* 	@param String email
	* 	@Return Mail
	 * */
    static Mail getMailPasswordHandling(String firstName, String lastName, String passwordToken, String email) {
		Map<String, Object> properties = new HashMap();
				properties.put("firstName", 	firstName);
				properties.put("lastName", 		lastName);
				properties.put("baseUrl", 		ApplicationConstants.WEBSITE_BASE_URL);
				properties.put("url", 			ApplicationConstants.WEBSITE_URL);
				properties.put("passwordToken", passwordToken);

		Mail mail = Mail.builder()
				.from(ApplicationConstants.WEBSITE_EMAIL_ADDRESS)
				.to(email)
				.htmlTemplate(new Mail.HtmlTemplate("passwordHandling", properties))
				.subject("Réinitialisation du mot de passe Warhammer Market")
				.build();

		return mail;
    }

    void sendEmail(Mail mail) throws MessagingException, IOException;

	static Mail getresetPswMail(String firstName, String lastName, String passwordToken, String email) {

        // This array contains the elements needed by the email that will be sent to the user
        Map<String, Object> properties = new HashMap();
					        properties.put("firstName", 	firstName);
					        properties.put("lastName", 		lastName);
					        properties.put("baseUrl", 		ApplicationConstants.WEBSITE_BASE_URL);
					        properties.put("url", 			ApplicationConstants.WEBSITE_URL);
					        properties.put("passwordToken", passwordToken);

        Mail mail = Mail.builder()
                .from(ApplicationConstants.WEBSITE_EMAIL_ADDRESS)
                .to(email)
                .htmlTemplate(new Mail.HtmlTemplate("passwordresettoken", properties))
                .subject("Réinitialisation du mot de passe Warhammer Market")
                .build();
		return mail;
	}

	static Mail getNotificationMail(String firstName, String lastName, String email) {
		
        Map<String, Object> properties = new HashMap();
        properties.put("firstName", firstName);
        properties.put("lastName", lastName);

        Mail mail = Mail.builder()
                .from(ApplicationConstants.WEBSITE_EMAIL_ADDRESS)
                .to(email)
                .htmlTemplate(new Mail.HtmlTemplate("passwordchanged", properties))
                .subject("Votre mot de passe Warhammer Market a changé")
                .build();
		return mail;
	}


	/*
	* 	 compose the contact us email
	* 		@param UserMessage wrapper
	*		@return Mail
	* */
	static Mail getWelcomeMail(String firstName, String lastName, String email){
		Map<String, Object> properties = new HashMap();
		properties.put("firstName", firstName);
		properties.put("lastName", lastName);

		Mail mail = Mail.builder()
				.from(ApplicationConstants.WEBSITE_EMAIL_ADDRESS)
				.to(email)
				.htmlTemplate(new Mail.HtmlTemplate("welcomeMessage", properties))
				.subject("Bienvenue sur Warhammer Market!")
				.build();
		return mail;
	}
	
	static Mail getTeamMail(UserMessage message) {
    	String subject = "message form webSite";

    	  Map<String, Object> properties = new HashMap();
          properties.put("email", message.getEmail());
          properties.put("subject", subject);
          properties.put("message", message.getMessage());
          
          Mail mail = Mail.builder()
    		       .from(ApplicationConstants.WEBSITE_EMAIL_ADDRESS)
                  .to(ApplicationConstants.WEBSITE_EMAIL_ADDRESS)
                  .htmlTemplate(new Mail.HtmlTemplate("userMessage", properties))
                  .subject(subject)
                  .build();
          
		return mail;
	}

}
