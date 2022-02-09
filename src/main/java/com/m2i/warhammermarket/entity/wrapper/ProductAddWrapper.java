package com.m2i.warhammermarket.entity.wrapper;

import java.math.BigDecimal;

import com.m2i.warhammermarket.entity.DAO.CategoryDAO;
import com.m2i.warhammermarket.entity.DAO.ProductDAO;
import com.m2i.warhammermarket.entity.DAO.UniverseDAO;

/**
 *  This class is create for use one object with all attributes that we need for the form on the front-end
 * @author Amal
 */
public class ProductAddWrapper {
	
	// Attributes class ProductDTO :
    private Long idProduct;
    private String ean13;
    private String labelProduct;
    private BigDecimal price;
    private String description;
    private float promotion;
    private int stock;
    private String picture;
    // Attributes class UniverseDTO :
    private Long idUniverse;
    private String refCodeUniverse;
    private String labelUniverse;
    // Attributes class CategoryDTO :
    private Long idCategory;
    private String refCodeCategory;
    private String labelCategory;
    
    // Personalised constructor for create a new object with all attributes in the same object.
    public ProductAddWrapper(ProductDAO product, UniverseDAO univers, CategoryDAO category){
    	
    	//Product
		this.ean13 = product.getEan13();
		this.labelProduct = product.getLabel();
		this.price = product.getPrice();
		this.description = product.getDescription();
		this.promotion = product.getPromotion();
		this.stock = product.getStock();
		this.picture = product.getPicture();
		//Universe
		this.refCodeUniverse = univers.getRefCode();
		this.labelUniverse = univers.getLabel();
		//Category
		this.refCodeCategory = category.getRefCode();
		this.labelCategory = category.getLabel();
			
	}
	public Long getIdProduct() {
		return idProduct;
	}
	public void setIdProduct(Long idProduct) {
		this.idProduct = idProduct;
	}
	public String getEan13() {
		return ean13;
	}
	public void setEan13(String ean13) {
		this.ean13 = ean13;
	}
	public String getLabelProduct() {
		return labelProduct;
	}
	public void setLabelProduct(String labelProduct) {
		this.labelProduct = labelProduct;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public float getPromotion() {
		return promotion;
	}
	public void setPromotion(float promotion) {
		this.promotion = promotion;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public Long getIdUniverse() {
		return idUniverse;
	}
	public void setIdUniverse(Long idUniverse) {
		this.idUniverse = idUniverse;
	}
	public String getRefCodeUniverse() {
		return refCodeUniverse;
	}
	public void setRefCodeUniverse(String refCodeUniverse) {
		this.refCodeUniverse = refCodeUniverse;
	}
	public String getLabelUniverse() {
		return labelUniverse;
	}
	public void setLabelUniverse(String labelUniverse) {
		this.labelUniverse = labelUniverse;
	}
	public Long getIdCategory() {
		return idCategory;
	}
	public void setIdCategory(Long idCategory) {
		this.idCategory = idCategory;
	}
	public String getRefCodeCategory() {
		return refCodeCategory;
	}
	public void setRefCodeCategory(String refCodeCategory) {
		this.refCodeCategory = refCodeCategory;
	}
	public String getLabelCategory() {
		return labelCategory;
	}
	public void setLabelCategory(String labelCategory) {
		this.labelCategory = labelCategory;
	}
	@Override
	public String toString() {
		return "ProductAddWrapper [idProduct=" + idProduct + ", ean13=" + ean13 + ", labelProduct=" + labelProduct
				+ ", price=" + price + ", description=" + description + ", promotion=" + promotion + ", stock=" + stock
				+ ", picture=" + picture + ", idUniverse=" + idUniverse + ", refCodeUniverse=" + refCodeUniverse
				+ ", labelUniverse=" + labelUniverse + ", idCategory=" + idCategory + ", refCodeCategory="
				+ refCodeCategory + ", labelCategory=" + labelCategory + "]";
	}
	
	

    

}
