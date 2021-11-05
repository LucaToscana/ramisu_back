package com.m2i.warhammermarket.service;

import com.m2i.warhammermarket.entity.DTO.ProductDTO;
import com.m2i.warhammermarket.entity.DTO.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

}
