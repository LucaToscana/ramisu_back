package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.controller.exception.UserMailAlreadyExistException;
import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.entity.DTO.UserSecurityDTO;
import com.m2i.warhammermarket.security.AuthorityConstant;
import com.m2i.warhammermarket.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

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
