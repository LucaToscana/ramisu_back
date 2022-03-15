package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.repository.UserRepository;
import com.m2i.warhammermarket.entity.DAO.UserDAO;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
    	
        UserDAO userDAO = userRepository.findByMail(mail);
        
    	if(userDAO == null) 	throw new UsernameNotFoundException("User not found with this mail : " + mail);
    
    	if(!userDAO.isActive())	throw new RuntimeException("");
        User utente = new User(userDAO.getMail(), userDAO.getPassword(),userDAO.getAuthorities());

        return utente;
    }
  
}