package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.entity.DAO.CategoryDAO;
import com.m2i.warhammermarket.entity.DAO.ProductDAO;
import com.m2i.warhammermarket.entity.DTO.CategoryDTO;
import com.m2i.warhammermarket.entity.wrapper.ProductAddWrapper;
import com.m2i.warhammermarket.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @CrossOrigin(origins = "*")
    @GetMapping("/public/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories(String field, String type) {
        List<CategoryDTO> list = this.categoryService.findAll();
        return ResponseEntity.ok().body(list);
    }
    
    
    /**
     * @param object category
     * @return A new category 
     * @author Amal
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/public/addcategory")
    public ResponseEntity<CategoryDAO> addCategory(@RequestBody ProductAddWrapper category){
    	return new ResponseEntity<>(categoryService.saveCategory(category),(HttpStatus.OK));
    	
    }
    
    
    
}
