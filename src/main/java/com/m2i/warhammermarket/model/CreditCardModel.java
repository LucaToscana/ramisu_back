package com.m2i.warhammermarket.model;

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

public class CreditCardModel {

	//private String email;
	private String cardStripe;
	private String last4;
	private String expiryDateMonth;
	private String expiryDateYear;
	private String brand;
	
	@Override
	public String toString() {
		return "CreditCardModel [last4=" + last4 + ", expiryDateMonth=" + expiryDateMonth + ", expiryDateYear="
				+ expiryDateYear + ", brand=" + brand + "]";
	}

	

}
