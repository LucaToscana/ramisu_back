package com.m2i.warhammermarket.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m2i.warhammermarket.entity.DTO.ProductDTO;
import com.m2i.warhammermarket.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @CrossOrigin(origins = "*")
    @GetMapping("/public/product/{id}")
    public ResponseEntity<Optional<ProductDTO>> getProduct(@PathVariable Long id) {
        Optional<ProductDTO> productDTO = this.productService.findOne(id);
        return ResponseEntity.ok().body(productDTO);
    }

    /**
     * Que fait la méthode
     * @param pageable
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/public/products/{field}/{type}")
    public ResponseEntity<List<ProductDTO>> getProductsSort(@PathVariable String field, @PathVariable String type){
        return ResponseEntity.ok().body(productService.getProductsSort(field, type));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/public/products")
    public ResponseEntity<List<ProductDTO>> getAllProduct(Pageable pageable) {
        Page<ProductDTO> page = this.productService.findAll(pageable);
        return ResponseEntity.ok().body(page.getContent());
    }
    
    
    /**
     * Search X number of products from a field
     * 
     * @param field the field of research
     * @param numberOfResult the number of products wanted
     * @return a page of X products
     * 
     * @author Cecile
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/public/products/random/{field}/{numberOfResult}")
    public ResponseEntity<List<ProductDTO>> getRandomProducts(@PathVariable String field, @PathVariable int numberOfResult) {
    	Page<ProductDTO> page = this.productService.findRandomProducts(field, numberOfResult);
    	if (page == null) {
    		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    	}
    	return ResponseEntity.ok().body(page.getContent());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/public/products/count")
    public ResponseEntity<Integer> ProductCount() {
        return ResponseEntity.ok().body(productService.productCounter());
    }

}
