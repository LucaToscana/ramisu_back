package com.m2i.warhammermarket.entity.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

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
	private String passwordTest;
	private String captchaToken;
	

	public UserSecurityDTO getUserSecurity() {
		 
		List<String> authorities = new ArrayList<String>();
		 authorities.add("user");
	        
		UserSecurityDTO user = new UserSecurityDTO();
						user.setAuthorities(authorities);
						user.setMail(this.getMail());
						user.setPassword(this.getPassword());
		return user;
	}

	public boolean isValid() {
		
		boolean validity = false;
		
		try {
			new InternetAddress(this.getMail());
		} catch (AddressException e) {
			return false;
		}
		
		
		if(this.password.equals(this.passwordTest))
		{
			
			  
			if(rxFieldValidity("^.*(?=.{8,})((?=.*[!@#$%^&*()\\-_=+{};:,<.>]))(?=.*\\d)((?=.*[a-z]))((?=.*[A-Z])).*$", this.password))
			{
				if(rxFieldValidity( "^([a-zA-Z -]|[à-úÀ-Ú])+$", getFirstName() ) && rxFieldValidity("^([a-zA-Z -]|[à-úÀ-Ú])+$", getLastName()))
				{
					validity = true;
				}
			}
		}
			
		return validity;
	}
	
	boolean rxFieldValidity(String rx, String value)
	{
		try {
			Pattern pattern= Pattern.compile(rx);
			Matcher matcher  = pattern.matcher(value);

			return matcher.find();
		}catch (PatternSyntaxException e) {
			e.printStackTrace();
			return false;
		}
		
	}
}
