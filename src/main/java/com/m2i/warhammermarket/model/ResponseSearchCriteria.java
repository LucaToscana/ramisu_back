package com.m2i.warhammermarket.model;

import java.util.List;

import com.m2i.warhammermarket.entity.DAO.ProductDAO;

public class ResponseSearchCriteria {

	public ResponseSearchCriteria(long totalElements, List<ProductDAO> content) {
		super();
		this.totalElements = totalElements;
		this.content = content;

	}

	private long totalElements;
	private List<ProductDAO> content;
	private String status = "success";
	private int statusCode = 200;

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public List<ProductDAO> getContent() {
		return content;
	}

	public void setContent(List<ProductDAO> content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	// Constructor, getters and setters omitted
}