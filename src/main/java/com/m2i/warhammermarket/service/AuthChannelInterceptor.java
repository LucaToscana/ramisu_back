package com.m2i.warhammermarket.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.m2i.warhammermarket.security.JwtUtil;
import com.m2i.warhammermarket.service.implement.JwtUserDetailService;

@Service
public class AuthChannelInterceptor implements ChannelInterceptor {
	private final JwtUtil jwtUtil;
	private final JwtUserDetailService jwtUserDetailService;
	private static final String TOKEN_HEADER = "token";

	@Autowired
	public AuthChannelInterceptor( JwtUtil jwtUtil,
			JwtUserDetailService jwtUserDetailService) {
		this.jwtUtil = jwtUtil;
		this.jwtUserDetailService = jwtUserDetailService;

	}

	// Processes a message before sending it
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {

		// Instantiate an object for retrieving the STOMP headers
		final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		// Check that the object is not null
		assert accessor != null;
		// If the frame is a CONNECT frame
		if (accessor.getCommand() == StompCommand.CONNECT) {

			// retrieve the username from the headers
			final String token = accessor.getFirstNativeHeader(TOKEN_HEADER);

			String mail = jwtUtil.getSubject(token);
			
			if (StringUtils.isNotBlank(mail)) {
				UserDetails userDetails = jwtUserDetailService.loadUserByUsername(mail);
				// On vérifie la validité du token selon la méthode de JwtUtil créé précédemment
				if (jwtUtil.isTokenValid(token, userDetails)) {
					// On crée l'authentication et on la set

					Authentication user = SecurityContextHolder.getContext().getAuthentication(); // access
																									// authentication
																									// header(s)

					accessor.setUser(user);

				}

			}

		}
		return message;
	}
}
