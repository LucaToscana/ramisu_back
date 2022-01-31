package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.entity.DTO.CategoryDTO;
import com.m2i.warhammermarket.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
}
