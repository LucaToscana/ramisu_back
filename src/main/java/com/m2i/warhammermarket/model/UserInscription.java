package com.m2i.warhammermarket.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;

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
public class UserInscription {
	/*
	 * nom:'', prenom:'', anniversaire:'', email:'', adresse:'', numeroA:'', rue:'',
	 * complementadresse:'', codepostal:'', ville:'', pays:'', telephone:'',
	 * password:'', passwordTest:'',
	 */
	private String nom;
	private String prenom;
	private String anniversaire/* orString */;
	private String email;
	private String adresse;
	private String numeroA;
	private String rue;
	private String complementadresse;
	private String codepostal;
	private String ville;
	private String pays;
	private String telephone;
	private String password;
	private String passwordTest;

}
