package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.configuration.ApplicationConstants;
import com.m2i.warhammermarket.controller.exception.UserMailAlreadyExistException;
import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.entity.DTO.UserInformationDTO;
import com.m2i.warhammermarket.entity.wrapper.ProfileWrapper;
import com.m2i.warhammermarket.entity.wrapper.RegistrationProfile;
import com.m2i.warhammermarket.entity.wrapper.UserMessage;
import com.m2i.warhammermarket.model.KeyAndPassword;
import com.m2i.warhammermarket.model.Mail;
import com.m2i.warhammermarket.security.AuthorityConstant;
import com.m2i.warhammermarket.service.EmailSenderService;
import com.m2i.warhammermarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.mail.MessagingException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
	
    @Autowired
    private UserService userService;


    @Autowired
    private EmailSenderService emailSenderService;

    /**
     * REST: {POST: /pictureProfile} 
     *
     * @param multipartFile profile picture to record in database and save in upload directory
     * @throws IOException
     * @return HttpStatus  OK, UNAUTHORIZED or BAD_REQUEST
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/public/pictureProfile", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> savePicture( @RequestParam("image") MultipartFile multipartFile) throws IOException
    {
    	MediaType mediaType = MediaType.parseMediaType(multipartFile.getContentType());
    	if(!mediaType.getType().equals("image"))return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    	
    	 UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	 ProfileWrapper userProfile = userService.getProfile(userDetails.getUsername());
	       
    	  if(userService.savePicture(userProfile, multipartFile) )return ResponseEntity.ok(HttpStatus.OK);
    	  else return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/public/removePictureProfile", method = RequestMethod.PUT)
    public ResponseEntity<HttpStatus> removePicture()
    {
    	 UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	 ProfileWrapper userProfile = userService.getProfile(userDetails.getUsername());
    	 userService.removePictureProfile(userProfile);
	       
		  return ResponseEntity.ok(HttpStatus.OK);
    	  
    }


    /**
     * Start the password recovery feature
     *
     * @param email the user email address needed for resetting its password
     * @return a ResponseEntity
     * @author Cecile
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/public/passwordresetstart")
    public ResponseEntity<String> resetPasswordStart(@RequestBody String email) {

        UserDTO userDTO = this.userService.findOneByUserMail(email);
        Optional<UserInformationDTO> userInformationDTO = null;

        if (userDTO != null && userDTO.isActive()) {
            userInformationDTO = this.userService.findUserInfoByUserMail(email);
            String passwordToken = this.userService.createPasswordResetToken(email);
            Mail mail =  EmailSenderService.getresetPswMail(userInformationDTO.get().getFirstName(), userInformationDTO.get().getLastName(), passwordToken , email);
            
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
    @CrossOrigin(origins = "*")
    @GetMapping("/public/passwordresetcheck/{key}")
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
    @CrossOrigin(origins = "*")
    @PostMapping("/public/passwordresetend")
    public ResponseEntity<HttpStatus> resetPasswordEnd(@RequestBody KeyAndPassword keyAndPassword) {

        //First we verify if keyAndPassword is present and check if the passwords match
        if (keyAndPassword != null && keyAndPassword.isValid()) {

            UserDTO userDTO = this.userService.findUserByPasswordResetToken(keyAndPassword.getKey());

            if (userDTO == null) return ResponseEntity.ok(HttpStatus.UNPROCESSABLE_ENTITY);

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

  
    @CrossOrigin(origins = "*")
    @GetMapping("/public/profile")
    public ResponseEntity<ProfileWrapper> getProfile() {
        return ResponseEntity
                .ok(userService.getProfile(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    /**
     * REST: {POST: /register} Controlleur pour pouvoir créer un nouveau compte
     * Vérifie d'abord si le compte existe ou non en BDD
     *
     * @return Long: id du compte créé
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/public/register")
    public ResponseEntity<Long> inscription(@RequestBody RegistrationProfile userProfile) {
        UserDTO userDTO = userService.findOneByUserMail(userProfile.getMail());
        if (userDTO != null) {
            throw new UserMailAlreadyExistException();
        }
        Long idSaved = userService.save(userProfile);
        return ResponseEntity.ok(idSaved);
    }
    
    @CrossOrigin("*")
    @PutMapping("/public/profile")
    public  ResponseEntity<HttpStatus> editProfile(@RequestBody ProfileWrapper profile ) {
    
    	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	// current user validity
    	boolean success = profile.getMail().equals(userDetails.getUsername());
    	if(!success) return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
    		try {
    			userService.updateProfile(profile);				
			} catch (Exception e) {
				return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			}
    	
    	return ResponseEntity.ok(HttpStatus.OK);
    }
    
    /**
     * 				
     * */
    @CrossOrigin("*")
    @PostMapping("/public/contactus")
    public  ResponseEntity<HttpStatus> editProfile(@RequestBody UserMessage message) {
    	
    	if(!message.valid())return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
    	
    	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String email = userDetails.getUsername();
    	ProfileWrapper profile =  userService.getProfile(email);
    	 Mail mail =  EmailSenderService.getTeamMail(profile , message);

          try {
			this.emailSenderService.sendEmail(mail);
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return ResponseEntity.ok(HttpStatus.OK);
    }
    
}
