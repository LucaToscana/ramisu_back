package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.entity.DTO.UserInformationDTO;
import com.m2i.warhammermarket.entity.wrapper.RegistrationProfile;
import com.m2i.warhammermarket.entity.wrapper.UserMessage;
import com.m2i.warhammermarket.model.KeyAndPassword;
import com.m2i.warhammermarket.model.Mail;
import com.m2i.warhammermarket.service.EmailSenderService;
import com.m2i.warhammermarket.service.ReCaptchaValidationService;
import com.m2i.warhammermarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;

import java.io.IOException;
import java.util.Optional;
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/public")
public class PublicController {
	@Autowired
	private ReCaptchaValidationService validator;

    @Autowired
    private UserService userService;
    

    @Autowired
    private EmailSenderService emailSenderService;



    /**
     * Start the password recovery feature
     *
     * @param email the user email address needed for resetting its password
     * @return a ResponseEntity
     * @author Cecile
     */
    @PostMapping("/passwordresetstart")
    public ResponseEntity<String> resetPasswordStart(@RequestBody String email) {

        UserDTO userDTO = this.userService.findOneByUserMail(email);
        Optional<UserInformationDTO> userInformationDTO = null;

        if (userDTO != null && userDTO.isActive()) {
            userInformationDTO = this.userService.findUserInfoByUserMail(email);
            String passwordToken = this.userService.createPasswordResetToken(email);
            Mail mail =  EmailSenderService.getresetPswMail(
                                                                userInformationDTO.get().getFirstName(),
                                                                userInformationDTO.get().getLastName(),
                                                                passwordToken ,
                                                                email
                                                            );
            try {
                this.emailSenderService.sendEmail(mail);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return ResponseEntity.ok("Mail envoyé");
    }

    /**
     * When the user is trying to reach the page to reset their password with a token,
     * check if the token exists and is valid.
     * If it does, the user can change their password,
     * else, the Front will put them back to the home page.
     *
     * @param key the password token used to reset a password
     * @return a ResponseEntity
     * @author Cecile
     */
    @GetMapping("/passwordresetcheck/{key}")
    public ResponseEntity<HttpStatus> checkTokenValidityOnOpeningResetPage(@PathVariable String key) {
        if (this.userService.findUserByPasswordResetToken(key) != null) {
            UserDTO userDTO = this.userService.findUserByPasswordResetToken(key);

            if ( userDTO!= null) {
                if (userDTO.getTokenExpiryDate() != null
                        && this.userService.isPasswordTokenValid(userDTO.getTokenExpiryDate())
                        && userDTO.isActive()) {
                    return ResponseEntity.ok(HttpStatus.OK);
                }
            }
        }
        return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    }

    /**
     * End the password recovery feature
     *
     * @param keyAndPassword the key allowing the change of the old password with the new one
     * @return a ResponseEntity
     * @author Cecile
     */
    @PostMapping("/passwordresetend")
    public ResponseEntity<HttpStatus> resetPasswordEnd(@RequestBody KeyAndPassword keyAndPassword) {

        //First we verify if keyAndPassword is present and check if the passwords match
        if (keyAndPassword != null && keyAndPassword.isValid()) {

            UserDTO userDTO = this.userService.findUserByPasswordResetToken(keyAndPassword.getKey());
            
            if (userDTO == null) return ResponseEntity.ok(HttpStatus.UNPROCESSABLE_ENTITY);

            userService.changeUserPasswordAndDeletePasswordToken(userDTO, keyAndPassword.getNewPassword());
            
            if (this.userService.isPasswordTokenValid(userDTO.getTokenExpiryDate()) && userDTO.isActive()) {
            	 Optional<UserInformationDTO> userInformationDTO =  this.userService.findUserInfoByUserMail(userDTO.getMail());
            	 Mail mail =  EmailSenderService.getNotificationMail(userInformationDTO.get().getFirstName() , userInformationDTO.get().getLastName(), userDTO.getMail());
                 
            	 
            	 
                 try {
                     this.emailSenderService.sendEmail(mail);
                     return ResponseEntity.ok(HttpStatus.OK);
                } catch (MessagingException e) {
                    e.printStackTrace();
                    return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
                } catch (IOException e) {
                    e.printStackTrace();
                    return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
            	return ResponseEntity.ok(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
        	return ResponseEntity.ok(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    /**
     * Send welcome mail upon registration
     *
     * @param UserDTO The new user who must be notified of the registration
     *
     * @author Loic
     */

    public void mailWelcome(UserDTO userDTO) {

        Optional<UserInformationDTO> userInformationDTO = null;

        if (userDTO != null && userDTO.isActive()) {
            try {
                userInformationDTO = this.userService.findUserInfoByUserMail(userDTO.getMail());
                Mail mail = EmailSenderService.getWelcomeMail(userInformationDTO.get().getFirstName(),userInformationDTO.get().getLastName(),userDTO.getMail());
                this.emailSenderService.sendEmail(mail);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * REST: {POST: /register} Controlleur pour pouvoir créer un nouveau compte
     * Vérifie d'abord si le compte existe ou non en BDD
     *
     * @return Long: id du compte créé
     */
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/register")
	public ResponseEntity<Long> inscription(@RequestBody RegistrationProfile userProfile) {

		Long idSaved = 0L;
		if (userProfile.isValid() && validator.validateCaptcha(userProfile.getCaptchaToken())) {

			UserDTO userDTO = userService.findOneByUserMail(userProfile.getMail());
            if (userDTO != null) return ResponseEntity.ok(-1L);//return -1 UserMailAlreadyExist in database

			idSaved = userService.save(userProfile);
            this.mailWelcome(userDTO);
		}
		return ResponseEntity.ok(idSaved);
	}


  
    
    /**
     * 		Send message from contact us form
     * @Param UserMessage
     * @Return HttpStatus
     * */
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/contactus")
    public  ResponseEntity<HttpStatus> editProfile(@RequestBody UserMessage message) {
    	if (validator.validateCaptcha(message.getCaptchaToken())) {
        	 Mail mail =  EmailSenderService.getTeamMail( message);
              try {
    			this.emailSenderService.sendEmail(mail);
    			System.out.println(message);  
    			System.out.println(mail);    			


    		} catch (MessagingException | IOException e) {
System.out.println(message);    			
e.printStackTrace();
    		}
        	
    		return ResponseEntity.ok(HttpStatus.OK);
    	}
    	
    	return ResponseEntity.ok(HttpStatus.FORBIDDEN);
    }

   
   
  
}

