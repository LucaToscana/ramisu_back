package com.m2i.warhammermarket.service;

import com.m2i.warhammermarket.entity.DTO.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<CategoryDTO> findAll();

    Optional<CategoryDTO> findOne(Long id);

    void delete(Long id);

    CategoryDTO saveCategoryDTO(CategoryDTO categoryDTO);
}
