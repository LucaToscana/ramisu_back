package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.entity.DTO.ProductDTO;
import com.m2i.warhammermarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping("/products/{id}")
    public ResponseEntity<Optional<ProductDTO>> getProduct(@PathVariable Long id) {
        Optional<ProductDTO> productDTO = this.productService.findOne(id);
        return ResponseEntity.ok().body(productDTO);
    }


}
