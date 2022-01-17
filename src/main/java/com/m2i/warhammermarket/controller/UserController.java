package com.m2i.warhammermarket.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

import javax.mail.MessagingException;

import com.m2i.warhammermarket.model.KeyAndPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.m2i.warhammermarket.configuration.ApplicationConstants;
import com.m2i.warhammermarket.controller.exception.UserMailAlreadyExistException;
import com.m2i.warhammermarket.controller.exception.UserNotFoundException;
import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.entity.DTO.UserInformationDTO;
import com.m2i.warhammermarket.entity.DTO.UserSecurityDTO;
import com.m2i.warhammermarket.entity.wrapper.ProfileWrapper;
import com.m2i.warhammermarket.model.Mail;
import com.m2i.warhammermarket.model.UserInscription;
import com.m2i.warhammermarket.security.AuthorityConstant;
import com.m2i.warhammermarket.service.EmailSenderService;
import com.m2i.warhammermarket.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;

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
    @Autowired
    private EmailSenderService emailSenderService;


    /**
     * REST: {POST: /register}
     * Controlleur pour pouvoir créer un nouveau compte
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

        if (userDTO == null) {
            throw new UserNotFoundException();
        } else {
            userInformationDTO = this.userService.findUserInfoByUserMail(email);
        }

        String passwordToken = this.userService.createPasswordResetToken(email);

        Map<String, Object> properties = new HashMap();
        properties.put("firstName", userInformationDTO.get().getFirstName());
        properties.put("lastName", userInformationDTO.get().getLastName());
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

            UserDTO userDTO = this.userService.findUserByPasswordResetToken(keyAndPassword.getKey());

            if (userDTO == null) return ResponseEntity.ok("Utilisateur non trouvé.");

            if (this.userService.isPasswordTokenValid(userDTO.getTokenExpiryDate())) {

                UserSecurityDTO userSecurityDTO = new UserSecurityDTO();
                userSecurityDTO.setPassword(keyAndPassword.getNewPassword());
                userSecurityDTO.setMail(userDTO.getMail());
                this.userService.changeUserPasswordAndDeletePasswordToken(userSecurityDTO);

                Optional<UserInformationDTO> userInformationDTO = this.userService.findUserInfoByUserMail(userSecurityDTO.getMail());

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
                return ResponseEntity.ok("Token non valide.");
            }
        } else {
            return ResponseEntity.ok("Erreur avec la clé ou les mots de passe.");
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
	/**
	 * Simpl controlleur sécurisé, pour tester le token JWT
	 *
	 * @return
	 */
	@GetMapping("/hello")
	public String hello() {
		return "Hello world";
	}

	@Secured({ AuthorityConstant.ROLE_USER, AuthorityConstant.ROLE_ADMIN })
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
		System.out.println(userInscription);
		Long idSaved = userService.saveInscription(userInscription);
		return ResponseEntity.ok(idSaved);
	}

}
