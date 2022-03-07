package com.m2i.warhammermarket.model;

public class UpdateStatusOrder {

	private long idOrder;
	private String status;

	public UpdateStatusOrder(long idOrder, String status) {
		super();
		this.idOrder = idOrder;
		this.status = status;
	}

	public UpdateStatusOrder() {
		super();
	}

	public long getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(long idOrder) {
		this.idOrder = idOrder;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
}
