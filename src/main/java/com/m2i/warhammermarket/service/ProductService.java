package com.m2i.warhammermarket.service;

import com.m2i.warhammermarket.entity.DAO.ProductDAO;
import com.m2i.warhammermarket.entity.DTO.ProductDTO;
import com.m2i.warhammermarket.entity.wrapper.ProductAddWrapper;
import com.m2i.warhammermarket.model.ProductRequestModel;
import com.m2i.warhammermarket.model.ProductSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    /**
     * Save a product in database
     * @param product the entity to save
     * @return the persisted entity
     */
    ProductDTO save (ProductDTO product);

    Page<ProductDTO> findAll(Pageable pageable);

    Optional<ProductDTO> findOne(Long id);


    Integer productCounter();

    List<ProductDTO> getProductsSort(String field, String type);
    
    
    /**
     * Search X number of products from a field
     * 
     * @param field the field of research (
     * @param numberOfResult the number of products wanted
     * @return a page of X products
     * 
     * @author Cecile
     */
    Page<ProductDTO> findRandomProducts(String field, int numberOfResult);

    /**
     * @param productSearchCriteria model for the critria filters
     * @return List of product filter by criteria query
     * @author Claire
     */
    List<ProductDAO> getProductCriteria(ProductSearchCriteria productSearchCriteria);
    
    
    /**
     * @param ProductAddWrapper object
     * @return A product 
     * @author Amal
     */
    public ProductDAO saveProduct(ProductAddWrapper productWrapper) ;
    
    
    /**
     * @param ProductAddWrapper object
     * @return A product udpated
     * @author Amal
     */    
    public ProductDAO updateProduct(ProductAddWrapper productWrapper);

    /**
     * @param Long id 
     * @author Amal
     */
	public void deleteProduct(Long id);

	
}


