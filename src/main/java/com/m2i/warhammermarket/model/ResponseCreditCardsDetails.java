package com.m2i.warhammermarket.model;

import java.util.List;

import com.m2i.warhammermarket.entity.DAO.ProductDAO;
import com.m2i.warhammermarket.entity.DTO.ProductDTO;

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
public class ResponseCreditCardsDetails {

	private List<CreditCardModel> listCards;
	private String status = "success";
	private int statusCode = 200;
	
	@Override
	public String toString() {
		return "ResponseCreditCardsDetails [size="  + ", listCards=" + listCards + ", status=" + status
				+ ", statusCode=" + statusCode + "]";
	}

	public ResponseCreditCardsDetails(List<CreditCardModel> listCards) {
		super();
		this.listCards = listCards;
	}
	
	

	
	
	
}