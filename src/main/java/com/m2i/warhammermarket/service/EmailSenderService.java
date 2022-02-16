package com.m2i.warhammermarket.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;

import com.m2i.warhammermarket.configuration.ApplicationConstants;
import com.m2i.warhammermarket.entity.DTO.UserInformationDTO;
import com.m2i.warhammermarket.entity.DTO.UserSecurityDTO;
import com.m2i.warhammermarket.entity.wrapper.ProfileWrapper;
import com.m2i.warhammermarket.entity.wrapper.UserMessage;
import com.m2i.warhammermarket.model.Mail;

public interface EmailSenderService {
	
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

	static Mail getTeamMail(UserMessage message) {
    	String subject = "message form webSite";
    	//	askcomm|askadm|askuser
//    		  switch (message.getSubject()) {
//    		  case "askadm":
//    			  subject = "Question à propos d'un compte utilisateur";
//					break;
//    		  case "askcomm":
//    			  subject = "Question d'ordre commercial";
//					break;
//    		  case "askuser":
//    			  subject = "Un utilisateur a constaté un bug sur le site";
//					break;
//		
//    		  default:
//    			  subject = "sujet non défini";
//				break;
//			}
    	   
    	  Map<String, Object> properties = new HashMap();
//          properties.put("firstName", firstName);
//          properties.put("lastName", lastName);
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
