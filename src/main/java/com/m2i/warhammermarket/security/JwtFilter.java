package com.m2i.warhammermarket.security;

import com.m2i.warhammermarket.service.implement.JwtUserDetailService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Classe permettant de créer notre propre Filtre de sécurité avec JSonWebToken
 *
 */
@Component
public class JwtFilter extends OncePerRequestFilter {
	@Value("${jwt.secret}")
	private String secret;
	
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtUserDetailService jwtUserDetailService;

    /**
     * Filter: on intercepte les Requêtes, et on vérifie le token
     * Si le token est bon, on set l'authentication
     * Sinon on ne fait rien
     *
     * Dans tout les cas, la requête est renvoyé tel quel
     *
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException

	{

		final String requestTokenHeader = request.getHeader("authorization");

		String username = null;
		String jwtToken = null;
		logger.warn(requestTokenHeader);
		logger.warn(requestTokenHeader);
		logger.warn(requestTokenHeader);
		logger.warn(requestTokenHeader);

		// JWT Token is in the form "Bearer token".
		// Remove Bearer word and get only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
		//	logger.warn("JWT Token does not begin  with Bearer String");
			jwtToken = requestTokenHeader.substring(7);
			try {
			//	logger.warn("JWT Token does not begin  with Bearer String");

				Claims claims = null;

				try {
					claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken).getBody();
					logger.warn(claims);
					logger.warn(claims);
					logger.warn(claims);
					logger.warn(claims);
					logger.warn(claims);


					username = claims.getSubject().toString();
				} catch (ExpiredJwtException | SignatureException e) {

				} catch (Exception e) {

				}

			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
		} else {
			logger.warn("JWT Token does not begin  with Bearer String");
		}
		// Once we get the token validate it.

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.jwtUserDetailService.loadUserByUsername(username);
			logger.warn(userDetails);
			logger.warn(userDetails);
			logger.warn(userDetails);
			logger.warn(userDetails);


			// if token is valid configure Spring Security to manually set
			// authentication
			
			logger.warn(jwtUtil.isTokenValid(jwtToken, userDetails));
			logger.warn(jwtUtil.isTokenValid(jwtToken, userDetails));
			logger.warn(jwtUtil.isTokenValid(jwtToken, userDetails));
			logger.warn(jwtUtil.isTokenValid(jwtToken, userDetails));
			logger.warn(jwtUtil.isTokenValid(jwtToken, userDetails));

			
			if (jwtUtil.isTokenValid(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

// After setting the Authentication in the context, we specify
// that the current user is authenticated. So it passes the
// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}			
		logger.warn(request.toString());
		logger.warn(response.toString());
		logger.warn(request.toString());
		logger.warn(response.toString());
		logger.warn(request.toString());
		logger.warn(response.toString());
		logger.warn(request.toString());
		logger.warn(response.toString());
		logger.warn(request.toString());
		logger.warn(response.toString());
		chain.doFilter(request, response);
	}
}