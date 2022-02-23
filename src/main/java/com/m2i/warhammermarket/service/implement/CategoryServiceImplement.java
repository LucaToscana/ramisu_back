package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.entity.DAO.CategoryDAO;
import com.m2i.warhammermarket.entity.DAO.ProductDAO;
import com.m2i.warhammermarket.entity.DTO.CategoryDTO;
import com.m2i.warhammermarket.entity.wrapper.ProductAddWrapper;
import com.m2i.warhammermarket.repository.CategoryRepository;
import com.m2i.warhammermarket.repository.ProductRepository;
import com.m2i.warhammermarket.service.CategoryService;
import com.m2i.warhammermarket.service.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImplement implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;
    
    @Autowired
    private ProductRepository productRepository;

    public CategoryServiceImplement() {

    }

    @Override
    public CategoryDTO save(CategoryDTO category) {
        return null;
    }

    @Override
    public List<CategoryDTO> findAll() {
        return this.categoryRepository.findAll().stream().map(categoryMapper::categoryToCategoryDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<CategoryDTO> findOne(Long id) {
        return Optional.ofNullable(this.categoryMapper.categoryToCategoryDTO(this.categoryRepository.findById(id).orElse(null)));
    }

    @Override
    public void delete(Long id) {

    }
    
    
    /**
     * @param category object
     *  This method is used to create a new category for the administrator
     *  @return category
     * @author Amal
     */ 
    @Override
    public CategoryDAO saveCategory(ProductAddWrapper categoryWrapper) {
    
    	CategoryDAO category= new CategoryDAO();
    	category.setId(categoryWrapper.getIdCategory());
    	category.setLabel(categoryWrapper.getLabelCategory());
    	category.setRefCode(categoryWrapper.getRefCodeCategory());
    	
    	//here we are checking if the category already exist by his name(label)
    	String label=category.getLabel();
    	
    	if(categoryRepository.findByLabel(label).isPresent()) {
    		return null;
    	}
    	
    	return categoryRepository.save(category);
    	
    }
    
    

    /**
     * @param a Long id 
     *  This method is used to delete a category for the administrator
     * @author Amal
     */ 
    
    public void deleteCategory(Long id) {
    	
    	CategoryDAO category = categoryRepository.getById(id);
    	List<ProductDAO> listproducts= new ArrayList<ProductDAO>();
    	
    	
    	
    	
    	
    }
    
    
    
}
