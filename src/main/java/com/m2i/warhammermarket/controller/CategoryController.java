package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.entity.DTO.CategoryDTO;
import com.m2i.warhammermarket.entity.DTO.UniverseDTO;
import com.m2i.warhammermarket.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories(String field, String type) {
        List<CategoryDTO> list = this.categoryService.findAll();
        return ResponseEntity.ok().body(list);
    }

    /**
     * @author Brice Bayard
     * Add a new category in table
     */
    @RequestMapping(value = "/commercial/addCategory", method = RequestMethod.POST)
    public ResponseEntity<CategoryDTO> createLabelCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO category = categoryService.saveCategoryDTO(categoryDTO);
            return ResponseEntity.ok().body(category);
        } catch (Exception e) {
            System.out.println("Exception dans createLabelCategory");
            return ResponseEntity.ok().body(null);
        }
    }
}
