package com.m2i.warhammermarket.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Classe permettant de regrouper de petites méthodes permettant de traiter le JWT
 *
 */
@Component
public class JwtUtil {

    private static final long JWT_VALIDITY_MILLISECOND = 1000 * 60 * 60 * 24;

    @Value("${jwt.secret}")
    private String secret;


    /**
     * Methode permettant de générer notre Token JWT
     * Celui contenant un "Claims", une liste de variable qui pourra être récupérer lorsque le BACK récupére le token depuis le front
     * Dans ce claims on a : Username, Date de Creation du token, et date d'expiration du Token
     *
     * @param userDetails, les info du user que l'on va utiliser pour générer le token
     * @return Le token généré
     */
    public String generateToken(UserDetails userDetails) {
        String authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        

        
        Map<String, Object> claims = new HashMap<>();
        //Si besoin, on peut rajouter des donner dans le claims ci-dessous
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .claim("AUTHORITIES_KEY", authorities)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_VALIDITY_MILLISECOND))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * Methode permettant de savoir si le Token est valide
     * 2 vérifications sont effectués:
     *  - Vérifie que le username dans le token est le même que celui nous a envoyé
     *  - Vérifie que le token n'a pas expiré
     *
     * @param token
     * @param userDetails
     * @return true si token valide
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return StringUtils.equalsIgnoreCase(getSubject(token), userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Methode permettant de récuperer le claims du token précisé
     *
     * @param token
     * @return le claims du token en param
     */
    private Claims getClaims(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * Récupére la date d'expériration du token en param
     *
     * @param token
     * @return la date d'expiration du token
     */
    private Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    /**
     * récupére le subject du token (dans notre cas, le username)
     *
     * @param token
     * @return username dans le claims du token
     */
    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Vérifie si le token est expiré ou non selon la date d'expiration de celui-ci et la date actuelle
     *
     * @param token
     * @return true si le token a expiré
     */
    private boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

}