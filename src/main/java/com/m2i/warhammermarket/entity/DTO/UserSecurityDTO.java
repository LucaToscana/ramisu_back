package com.m2i.warhammermarket.entity.DTO;

import lombok.*;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserSecurityDTO {
    private String mail;
    private String password;
    private Long id;
    private List<String> authorities = new ArrayList<>();

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Long getId() {
        return id;
    }

	public UserSecurityDTO(String mail, String password, List<String> authorities) {
		super();
		this.mail = mail;
		this.password = password;
		this.authorities = authorities;
	}
}