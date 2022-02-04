package com.m2i.warhammermarket.model;

import java.util.List;

import com.m2i.warhammermarket.entity.DAO.ProductDAO;
import com.m2i.warhammermarket.entity.DTO.ProductDTO;

public class ResponseProductDetails {

	private ProductDTO content;
	private List<ProductDTO> relatedContent;

	private long totalElements;
	private String status = "success";
	private int statusCode = 200;
	public ProductDTO getContent() {
		return content;
	}
	public void setContent(ProductDTO content) {
		this.content = content;
	}
	public List<ProductDTO> getRelatedContent() {
		return relatedContent;
	}
	public void setRelatedContent(List<ProductDTO> relatedContent) {
		this.relatedContent = relatedContent;
	}
	public long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
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
	public ResponseProductDetails(ProductDTO content, List<ProductDTO> relatedContent, long totalElements) {
		super();
		this.content = content;
		this.relatedContent = relatedContent;
		this.totalElements = totalElements;

	}

	
	
	
}