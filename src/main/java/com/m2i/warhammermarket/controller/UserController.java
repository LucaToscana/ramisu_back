package com.m2i.warhammermarket.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.m2i.warhammermarket.controller.exception.UserMailAlreadyExistException;
import com.m2i.warhammermarket.controller.exception.UserNotFoundException;
import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.entity.DTO.UserSecurityDTO;
import com.m2i.warhammermarket.security.AuthorityConstant;
import com.m2i.warhammermarket.service.EmailSenderService;
import com.m2i.warhammermarket.service.UserService;

import model.Mail;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;
    
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
        if(userDTO != null) {
            throw new UserMailAlreadyExistException();
        }
        Long idSaved = userService.save(userSecurity);
        return ResponseEntity.ok(idSaved);
    }
    
    
    @PostMapping("/user/resetPassword")
    public ResponseEntity resetPassword(@RequestParam("email") String userEmail) {
        UserDTO userDTO = this.userService.findOneByUserMail(userEmail);
        if (userDTO == null) {
            throw new UserNotFoundException();
        }
        String token = UUID.randomUUID().toString();
        this.userService.createPasswordResetToken(userDTO, token);

        Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("firstName", "John Michel!");
		properties.put("location", "Sri Lanka");
		properties.put("sign", "Java Developer");

		Mail mail = Mail.builder()
				.from("testfrom@gmail.com")
				.to("wnsfernando95@gmail.com")
				.htmlTemplate(new Mail.HtmlTemplate("sample", properties))
				.subject("This is sample email with spring boot and thymeleaf")
				.build();
		this.emailSenderService.sendMail(mail);
		
		return (ResponseEntity) ResponseEntity.ok();
		/*
        return new ResponseEntity(
          messages.getMessage("message.resetPasswordEmail", null, 
          request.getLocale()));*/
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
}
