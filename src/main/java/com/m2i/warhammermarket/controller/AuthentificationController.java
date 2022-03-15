package com.m2i.warhammermarket.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.m2i.warhammermarket.entity.DTO.AuthentificationResponseDTO;
import com.m2i.warhammermarket.entity.DTO.UserSecurityDTO;
import com.m2i.warhammermarket.security.JwtUtil;
import com.m2i.warhammermarket.service.NotificationService;
import com.m2i.warhammermarket.service.implement.JwtUserDetailService;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class AuthentificationController {
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private JwtUserDetailService userDetailService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	


	/**
	 * Request REST: {POST /login} Permet de se connecter, si on reçoit des
	 * Credentials correctes Si cela est bon, envoi au front un objet avec le
	 * JsonWebToken et le username de l'utilisateur
	 *
	 * @param userSecurity
	 * @return AuthenticationResponse
	 * @throws Exception
	 */
	@PostMapping("/api/public/login")
	public ResponseEntity<AuthentificationResponseDTO> authenticate(@RequestBody UserSecurityDTO userSecurity)
			throws Exception {

		UsernamePasswordAuthenticationToken tokenSpring = new UsernamePasswordAuthenticationToken(userSecurity.getMail(), userSecurity.getPassword());
		try {
			// L'authentification se fait ici. C'est SpringSecurity qui vérifie les
			// Crédentials reçu, et va pouvoir m'authentifier
			// Dans le cas contraire, renvoi une exception BadCredentialsException
			authenticationManager.authenticate(tokenSpring);
		} catch (BadCredentialsException ex) {
			 throw ex;
			//return null;

		} catch (DisabledException ex) {
			 throw ex;
			//return null;

		} catch (LockedException ex) { throw ex;
			//return null;

		} catch (RuntimeException ex) {
			
			return ResponseEntity.ok(new AuthentificationResponseDTO(null, userSecurity.getMail(),"Le compte " + userSecurity.getMail() + " est	désactivé."));

		}
		// Je récupére le UserDetails coresspondant au username reçu

		UserDetails userDetails = userDetailService.loadUserByUsername(userSecurity.getMail());
		// Avec UserDetails, je crée mon JsonWebToken
		String jwt = jwtUtil.generateToken(userDetails);
		// Je renvoi au front l'objet AuthenticationResponse contenant le JWT et
		// username de l'utilisateur
		// Toutes requetes sécurisé en back aura donc besoin de ce JWT pour être
		// effectué
		
		
		
		return ResponseEntity.ok(new AuthentificationResponseDTO(jwt, userDetails.getUsername(),null));

	}

    
}
