package com.m2i.warhammermarket.service;

import com.m2i.warhammermarket.entity.DTO.ProductDTO;
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

    void delete(Long id);

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

    //List <ProductDTO> searchByCriteria (ProductSearchRequestModel productSearchRequestModel) ;

}
