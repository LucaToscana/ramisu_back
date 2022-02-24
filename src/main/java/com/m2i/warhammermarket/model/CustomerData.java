package com.m2i.warhammermarket.model;


import java.math.BigDecimal;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CustomerData {

	private String cardHolder;
	private String email;
	private String cardNumber;
	private String cvc;
	private String expiryDate;
	private double amount;
	@Override
	public String toString() {
		return "CunstomerData [cardHolder=" + cardHolder + ", email=" + email + ", cardNumber=" + cardNumber + ", cvc="
				+ cvc + ", expiryDate=" + expiryDate + ", amount=" + amount + "]";
	}


	
	
}