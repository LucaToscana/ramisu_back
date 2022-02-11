package com.m2i.warhammermarket.entity.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.m2i.warhammermarket.entity.DTO.UserSecurityDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegistrationProfile extends ProfileWrapper {
	private String password;
	private String captchaToken;

	public UserSecurityDTO getUserSecurity() {
		 
		List<String> authorities = new ArrayList<String>();
		 authorities.add("USER");
	        
		UserSecurityDTO user = new UserSecurityDTO();
						user.setAuthorities(authorities);
						user.setMail(this.getMail());
						user.setPassword(this.getPassword());
		return user;
	}
}
