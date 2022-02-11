package com.m2i.warhammermarket.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.entity.wrapper.ProfileWrapper;
import com.m2i.warhammermarket.repository.UserRepository;
import com.m2i.warhammermarket.service.UserService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RestController

@RequestMapping("/api/user/")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserService userService;

	  /**
     * Simpl controlleur sécurisé, pour tester le token JWT
     *
     * @return
     */
   
//    @Secured({AuthorityConstant.ROLE_USER, AuthorityConstant.ROLE_ADMIN})
    @GetMapping("/hello-user")
    public String helloAdmin() {
        return "Hello user";
    }

    @GetMapping("/hello-saileman")
    public String helloSailman() {
        return "Hello user";
    }

//    @Secured(AuthorityConstant.ROLE_ADMIN)
    @GetMapping("/hello-admin")
    public String helloUser() {
        return "Hello admin";
    }

    
	@GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
			Page<UserDTO> users = userService.findAll(pageable);
		return ResponseEntity.ok().body(users);
	}

 
	@GetMapping("/account/{id}")
    public ProfileWrapper getuserAccount(@PathVariable Long id) {		
			Optional<UserDTO>	user = userService.findOne(id);
			ProfileWrapper 		profile = userService.getProfile(user.get().getMail());
		return profile;
	
	}

	@PutMapping("/account/roles/")
	public ResponseEntity<HttpStatus> updateUserRoles(@RequestBody UserRoles roles)
	{
			userService.updateRoles(roles.getUserID() , roles.getRolesID(), roles.isActive());
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
}

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
	class UserRoles
	{
		private Long 	userID;
		private Long 	rolesID;
		private boolean active;
	}