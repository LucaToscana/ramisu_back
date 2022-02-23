package com.m2i.warhammermarket.service;

import com.m2i.warhammermarket.entity.DAO.CategoryDAO;
import com.m2i.warhammermarket.entity.DTO.CategoryDTO;
import com.m2i.warhammermarket.entity.wrapper.ProductAddWrapper;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    CategoryDTO save (CategoryDTO category);

    List<CategoryDTO> findAll();

    Optional<CategoryDTO> findOne(Long id);

    void delete(Long id);

	/**
	 * @param category object
	 *  This method is used to create a new category.
	 *  @return category
	 * @author Amal
	 */
	CategoryDAO saveCategory(ProductAddWrapper categoryWrapper);
}
