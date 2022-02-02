package com.m2i.warhammermarket.model;

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
    	 return newPassword != null && verifyPassword != null && newPassword.equals(verifyPassword);
         
    }

}
