package com.m2i.warhammermarket.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyAndPassword {

    private String key;
    private String newPassword;
    private String verifyPassword;
    
    
    public boolean isValid()
    {
    	if(newPassword != null && verifyPassword != null && newPassword.equals(verifyPassword))
		{
		  Pattern pattern = Pattern.compile("^.*(?=.{8,})((?=.*[!@#$%^&*()\\-_=+{};:,<.>]))(?=.*\\d)((?=.*[a-z]))((?=.*[A-Z])).*$");
	        Matcher matcher  = pattern.matcher(newPassword);

	        //    validation schema failed
	        if(matcher.find())return true; 
		}
    	return false;
    }

}
