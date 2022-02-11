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
        if(userDAO == null) throw new UsernameNotFoundException("User not found with this mail : " + mail);
        return new User(userDAO.getMail(), userDAO.getPassword(),getAuthority( userDAO));
    }
   
    private Set<SimpleGrantedAuthority> getAuthority(UserDAO user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<SimpleGrantedAuthority>();
        user.getAuthorities().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getAuthority()));
        });
        return authorities;
    }
}