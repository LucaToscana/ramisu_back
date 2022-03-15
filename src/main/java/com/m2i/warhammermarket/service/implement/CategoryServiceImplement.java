package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.entity.DAO.CategoryDAO;
import com.m2i.warhammermarket.entity.DTO.CategoryDTO;
import com.m2i.warhammermarket.repository.CategoryRepository;
import com.m2i.warhammermarket.service.CategoryService;
import com.m2i.warhammermarket.service.mapper.CategoryMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImplement implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    public CategoryServiceImplement() {

    }

    @Override
    public List<CategoryDTO> findAll() {
        // return this.categoryRepository.findAll().stream().map(categoryMapper::categoryToCategoryDTO).collect(Collectors.toList());
        return this.categoryRepository.findAll().stream().map(e->modelMapper.map(e, CategoryDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Optional<CategoryDTO> findOne(Long id) {
        // return Optional.ofNullable(this.categoryMapper.categoryToCategoryDTO(this.categoryRepository.findById(id).orElse(null)));
        return Optional.ofNullable(modelMapper.map(this.categoryRepository.findById(id).orElse(null), CategoryDTO.class));
    }

    @Override
    public void delete(Long id) {

    }

    /**
     * @param categoryDTO object
     *  This method is used to create a new universe from the administrator
     *  @return category
     * @author Brice
     */
    @Override
    public CategoryDTO saveCategoryDTO(CategoryDTO categoryDTO) {
        // Instantiates the recovered object in the DAO to attach the attributes/fields to it
        CategoryDAO category = categoryMapper.categoryDTOToCategoryDAO(categoryDTO);
        // Use Hibernate.SQL to prepare insert into table
        CategoryDAO categorySave = categoryRepository.save(category);
        //
        CategoryDTO categoryResult = categoryMapper.categoryDAOToCategoryDTO(categorySave);
        // Return the result
        return categoryResult;
    }
}
