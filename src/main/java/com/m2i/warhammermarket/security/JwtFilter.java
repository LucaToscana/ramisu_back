package com.m2i.warhammermarket.security;

import com.m2i.warhammermarket.service.implement.JwtUserDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        //On récupére le token (null sinon)
        String token = resolveToken(request);
        //On vérifie que le token existe bien, donc non null
        if(StringUtils.isNotBlank(token)) {
            String username = jwtUtil.getSubject(token);
            //On vérifie que le token a bien un username et que l'Authentication n'est pas déjà fait
            if(StringUtils.isNotBlank(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = jwtUserDetailService.loadUserByUsername(username);
                //On vérifie la validité du token selon la méthode de JwtUtil créé précédemment
                if(jwtUtil.isTokenValid(token, userDetails)) {
                    //On crée l'authentication et on la set
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(auth);


                }
            }
        }
        //On renvoit toujours la requete et la réponse
        chain.doFilter(request, response);
    }

    /**
     * Permet de récupérer le token depuis une requête
     * Envoi nul si token non existant, ou si incohérent
     *
     * @param request
     * @return le token, ou null si non existant ou incohérent
     */
    private String resolveToken(HttpServletRequest request) {

        String authToken = request.getHeader("Authorization");
        


        if(StringUtils.isNotBlank(authToken) && authToken.startsWith("Bearer ")) {

            return authToken.substring(7);
        }
        return null;
    }
}