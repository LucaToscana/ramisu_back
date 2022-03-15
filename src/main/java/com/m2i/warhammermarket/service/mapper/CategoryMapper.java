package com.m2i.warhammermarket.service.mapper;


import com.m2i.warhammermarket.entity.DAO.CategoryDAO;
import com.m2i.warhammermarket.entity.DAO.UniverseDAO;
import com.m2i.warhammermarket.entity.DTO.CategoryDTO;
import com.m2i.warhammermarket.entity.DTO.UniverseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoryMapper {

    public List<CategoryDTO> categoriesToCategoriesDTOList(List<CategoryDAO> categoryDAOS) {
        return categoryDAOS.stream()
                .filter(Objects::nonNull)
                .map(this::categoryToCategoryDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO categoryDAOToCategoryDTO(CategoryDAO categoryDAO) {
        return new CategoryDTO(categoryDAO);
    }

    public CategoryDTO categoryToCategoryDTO(CategoryDAO categoryDAO) {
        return new CategoryDTO(categoryDAO);
    }

    public List<CategoryDAO> categoryDTOsToCategories(List<CategoryDTO> categoryDTOs) {
        return categoryDTOs.stream()
                .filter(Objects::nonNull)
                .map(this::categoryDTOToCategory)
                .collect(Collectors.toList());
    }

    public CategoryDAO categoryDTOToCategory(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        } else {
            CategoryDAO categoryDAO = new CategoryDAO();
            categoryDAO.setId(categoryDAO.getId());
            categoryDAO.setRefCode(categoryDAO.getRefCode());
            categoryDAO.setLabel(categoryDAO.getLabel());
            return categoryDAO;
        }
    }

    public CategoryDAO categoryDTOToCategoryDAO(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        } else {
            CategoryDAO categoryDAO = new CategoryDAO();
            categoryDAO.setId(categoryDTO.getId());
            categoryDAO.setRefCode(categoryDTO.getRefCode());
            categoryDAO.setLabel(categoryDTO.getLabel());
            return categoryDAO;
        }
    }

    public CategoryDAO categoryFromId(Long id) {
        if (id == null) {
            return null;
        }
        CategoryDAO categoryDAO = new CategoryDAO();
        categoryDAO.setId(id);
        return categoryDAO;
    }
}
