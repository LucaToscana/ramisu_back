package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.entity.DTO.ProductDTO;
import com.m2i.warhammermarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @CrossOrigin(origins = "*")
    @GetMapping("/public/products/{id}")
    public ResponseEntity<Optional<ProductDTO>> getProduct(@PathVariable Long id) {
        Optional<ProductDTO> productDTO = this.productService.findOne(id);
        return ResponseEntity.ok().body(productDTO);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/public/products")
    public ResponseEntity<List<ProductDTO>> getAllProduct(Pageable pageable) {
        Page<ProductDTO> page = this.productService.findAll(pageable);
        return ResponseEntity.ok().body(page.getContent());
    }

}
