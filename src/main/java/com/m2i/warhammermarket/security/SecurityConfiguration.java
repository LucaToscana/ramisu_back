package com.m2i.warhammermarket.security;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * Classe permettant de Configurer la Securité de SpringSecurity
 * Le nom SecurityConfiguration est important, c'est un mot clé
 *
 */
@Configuration
@EnableWebSecurity //Annotation permettant d'overrider SpringSecurity, et de créer notre propre securité (dans ce projet, avec JWT)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtFilter jwtRequestFilter;


    /**
     * Methode de configuration permettant d'associer les services natifes de SpringSecurity avec la d'encryptage choisi
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * Methode pour spécifier quelle est la classe d'encryptage de Mot De Passe
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH","OPTIONS")
				.allowedHeaders("*");
	}

	
    /**
     * Methode pour spécifier quel manager est utilisé pour gérer l'Authentication
     * Ici, on utilise par défaut AuthenticationManager de SpringSecurity
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
   
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    
    /**
     * Methode principale, permettant de spécifier les configuration principal de notre application
     * Notamment les routes Privé et Public, ou le CORS, ou désactivation de CSRF, etc ...
     *
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and()
                .csrf().disable().addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(
                		"/ws/**"
                		)
                		.permitAll()
                .antMatchers("/api/public/**").permitAll()
                .antMatchers("/upload/profilePictures/**").permitAll()
                .antMatchers("/api/user/**").hasAuthority(AuthorityConstant.ROLE_USER)
                .antMatchers("/api/admin/**").hasAuthority(AuthorityConstant.ROLE_ADMIN)
                .antMatchers("/api/commercial/**").hasAuthority(AuthorityConstant.ROLE_COMM)
                .antMatchers("/api/orders/**").hasAuthority(AuthorityConstant.ROLE_USER)
//On créer des routes publiques (on peut également avoir des regex globaux tel que "/public/**")
                .anyRequest().authenticated() //on définie que toutes les routes ne correspondant pas aux routes du dessus sont sécurisé par authentification
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //on spécifie à SpringSecurity de ne pas créer de HttpSession

    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        // Tell Spring to ignore securing the handshake endpoint. This allows the handshake to take place unauthenticated
        web.ignoring().antMatchers("/ws/**");
    }
}