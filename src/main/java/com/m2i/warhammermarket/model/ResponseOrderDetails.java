package com.m2i.warhammermarket.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.m2i.warhammermarket.entity.DAO.AuthorityDAO;
import com.m2i.warhammermarket.entity.DAO.ProductDAO;
import com.m2i.warhammermarket.entity.DTO.OrderDTO;
import com.m2i.warhammermarket.entity.DTO.ProductDTO;
import com.m2i.warhammermarket.entity.wrapper.ProductOrderWrapper;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ResponseOrderDetails {

	
	private OrderDTO content;
	private List<ProductOrderWrapper> productOrderWrappers;
	private String status = "success";
	private int statusCode = 200;

	
	public ResponseOrderDetails(OrderDTO content, List<ProductOrderWrapper> productOrderWrappers) {
		super();
		this.content = content;
		this.productOrderWrappers = productOrderWrappers;
	}
	
	
	
	
}