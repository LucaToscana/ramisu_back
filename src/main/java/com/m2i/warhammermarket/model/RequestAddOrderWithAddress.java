package com.m2i.warhammermarket.model;

import java.util.List;

import com.m2i.warhammermarket.entity.DAO.AddressDAO;
import com.m2i.warhammermarket.entity.wrapper.ProductOrderWrapper;

/*const order = {
        list:list,
        addresse:addresse,
        type:type
    }*/
public class RequestAddOrderWithAddress {

	List<ProductOrderWrapper> productsOrder;
	AddressDAO address;
	String type;
	String isMain;
	public RequestAddOrderWithAddress(List<ProductOrderWrapper> productsOrder, AddressDAO address, String type,String isMain) {
		super();
		this.productsOrder = productsOrder;
		this.address = address;
		this.type = type;
		this.isMain = isMain;
	}
	public RequestAddOrderWithAddress() {
		super();
	}
	public List<ProductOrderWrapper> getProductsOrder() {
		return productsOrder;
	}
	public void setProductsOrder(List<ProductOrderWrapper> productsOrder) {
		this.productsOrder = productsOrder;
	}
	public AddressDAO getAddress() {
		return address;
	}
	public void setAddress(AddressDAO address) {
		this.address = address;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsMain() {
		return isMain;
	}
	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}
	@Override
	public String toString() {
		return "RequestAddOrderWithAddress [productsOrder=" + productsOrder + ", address=" + address + ", type=" + type
				+ ", isMain=" + isMain + "]";
	}
	
	
	
	
	
}
