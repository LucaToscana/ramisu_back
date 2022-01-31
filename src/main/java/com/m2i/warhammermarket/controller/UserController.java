package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.configuration.ApplicationConstants;
import com.m2i.warhammermarket.controller.exception.UserMailAlreadyExistException;
import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.entity.DTO.UserInformationDTO;
import com.m2i.warhammermarket.entity.DTO.UserSecurityDTO;
import com.m2i.warhammermarket.entity.wrapper.ProfileWrapper;
import com.m2i.warhammermarket.model.KeyAndPassword;
import com.m2i.warhammermarket.model.Mail;
import com.m2i.warhammermarket.model.UserInscription;
import com.m2i.warhammermarket.security.AuthorityConstant;
import com.m2i.warhammermarket.service.EmailSenderService;
import com.m2i.warhammermarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class UserController {
	

    @Autowired
    private UserService userService;


    @Autowired
    private EmailSenderService emailSenderService;

    private static class UserControllerException extends RuntimeException {
        private UserControllerException(String message) {
            super(message);
        }
    }
    /**
     * REST: {POST: /register} 
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
     * REST: {POST: /register} Controlleur pour pouvoir créer un nouveau compte
     * Vérifie d'abord si le compte existe ou non en BDD
     *
     * @param userSecurity
     * @return Long: id du compte créé
     */
    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody UserSecurityDTO userSecurity) {
        UserDTO userDTO = userService.findOneByUserMail(userSecurity.getMail());
        if (userDTO != null) {
            throw new UserMailAlreadyExistException();
        }
        Long idSaved = userService.save(userSecurity);
        return ResponseEntity.ok(idSaved);
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
        } else {
            return ResponseEntity.ok("Mail envoyé");
        }

        String passwordToken = this.userService.createPasswordResetToken(email);

        // This array contains the elements needed by the email that will be sent to the user
        Map<String, Object> properties = new HashMap();
					        properties.put("firstName", userInformationDTO.get().getFirstName());
					        properties.put("lastName", userInformationDTO.get().getLastName());
					        properties.put("baseUrl", ApplicationConstants.WEBSITE_BASE_URL);
					        properties.put("url", ApplicationConstants.WEBSITE_URL);
					        properties.put("passwordToken", passwordToken);

        Mail mail = Mail.builder()
                .from(ApplicationConstants.WEBSITE_EMAIL_ADDRESS)
                .to(email)
                .htmlTemplate(new Mail.HtmlTemplate("passwordresettoken", properties))
                .subject("Réinitialisation du mot de passe Warhammer Market")
                .build();
        try {
            this.emailSenderService.sendEmail(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
    public ResponseEntity<String> checkTokenValidityOnOpeningResetPage(@PathVariable String key) {
        if (this.userService.findUserByPasswordResetToken(key) != null) {
            UserDTO userDTO = this.userService.findUserByPasswordResetToken(key);

            if ( userDTO!= null) {
                if (userDTO.getTokenExpiryDate() != null
                        && this.userService.isPasswordTokenValid(userDTO.getTokenExpiryDate())
                        && userDTO.isActive()) {
                    return ResponseEntity.ok("");
                }
            }
        }
        throw new UserControllerException("Ce lien n'est pas valide");
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
    public ResponseEntity<String> resetPasswordEnd(@RequestBody KeyAndPassword keyAndPassword) {

        //First we verify if keyAndPassword is present and check if the passwords match
        if (keyAndPassword != null && (keyAndPassword.getKey() != null
                && keyAndPassword.getNewPassword() != null && keyAndPassword.getVerifyPassword() != null)
                && (keyAndPassword.getNewPassword().equals(keyAndPassword.getVerifyPassword()))
        ) {

            //We check if the password is as complex as asked
            Pattern pattern;
            Matcher matcher;
            pattern = Pattern.compile("^.*(?=.{8,})((?=.*[!@#$%^&*()\\-_=+{};:,<.>]))(?=.*\\d)((?=.*[a-z]))((?=.*[A-Z])).*$");
            matcher = pattern.matcher(keyAndPassword.getNewPassword());

            if(!matcher.find()) {
                throw new UserControllerException("Le mot de passe doit contenir au moins 8 caractères, "
                        + "une majuscule, un nombre et un caractère spécial.");
            }

            UserDTO userDTO = this.userService.findUserByPasswordResetToken(keyAndPassword.getKey());

            if (userDTO == null) throw new UserControllerException("Utilisateur non trouvé.");

            if (this.userService.isPasswordTokenValid(userDTO.getTokenExpiryDate()) && userDTO.isActive()) {

                // We put the new password and email address into an UserSecurityDTO instance,
                // then snd it to the service layer where the new password will replace the old one
                // and the token will be deleted
                UserSecurityDTO userSecurityDTO = new UserSecurityDTO();
                userSecurityDTO.setPassword(keyAndPassword.getNewPassword());
                userSecurityDTO.setMail(userDTO.getMail());
                this.userService.changeUserPasswordAndDeletePasswordToken(userSecurityDTO);

                Optional<UserInformationDTO> userInformationDTO =
                        this.userService.findUserInfoByUserMail(userSecurityDTO.getMail());

                Map<String, Object> properties = new HashMap();
                properties.put("firstName", userInformationDTO.get().getFirstName());
                properties.put("lastName", userInformationDTO.get().getLastName());

                Mail mail = Mail.builder()
                        .from(ApplicationConstants.WEBSITE_EMAIL_ADDRESS)
                        .to(userDTO.getMail())
                        .htmlTemplate(new Mail.HtmlTemplate("passwordchanged", properties))
                        .subject("Votre mot de passe Warhammer Market a changé")
                        .build();
                try {
                    this.emailSenderService.sendEmail(mail);
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return ResponseEntity.ok("Mot de passe changé et mail envoyé.");
            } else {
                throw new UserControllerException("Erreur de traitement.");
            }
        } else {
            throw new UserControllerException("Erreur de traitement.");
        }
    }

    /**
     * Simpl controlleur sécurisé, pour tester le token JWT
     *
     * @return
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello world";
    }

    @Secured({AuthorityConstant.ROLE_USER, AuthorityConstant.ROLE_ADMIN})
    @GetMapping("/hello-user")
    public String helloAdmin() {
        return "Hello user";
    }

    @Secured(AuthorityConstant.ROLE_ADMIN)
    @GetMapping("/hello-admin")
    public String helloUser() {
        return "Hello admin";
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

    @PostMapping("/inscription")
    public ResponseEntity<Long> inscription(@RequestBody UserInscription userInscription) {
        UserDTO userDTO = userService.findOneByUserMail(userInscription.getEmail());
        if (userDTO != null) {
            throw new UserMailAlreadyExistException();
        }
        Long idSaved = userService.saveInscription(userInscription);
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

}
