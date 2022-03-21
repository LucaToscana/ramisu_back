package com.m2i.warhammermarket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.m2i.warhammermarket.entity.DAO.ProductDAO;
import com.m2i.warhammermarket.entity.DTO.ProductDTO;
import com.m2i.warhammermarket.model.ProductRequestModel;
import com.m2i.warhammermarket.model.ProductSearchCriteria;
import com.m2i.warhammermarket.model.ResponseProductDetails;
import com.m2i.warhammermarket.model.ResponseSearchCriteria;
import com.m2i.warhammermarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")

public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping("/public/product/{id}")
	public ResponseEntity<Optional<ProductDTO>> getProduct(@PathVariable Long id) {
		Optional<ProductDTO> productDTO = this.productService.findOne(id);
		return ResponseEntity.ok().body(productDTO);
	}

	@GetMapping("/public/productDetails/{id}")
	public ResponseEntity<Optional<ResponseProductDetails>> getProductDetails(@PathVariable Long id) {
		Optional<ResponseProductDetails> response = this.productService.findOneDetails(id);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Que fait la méthode
	 * 
	 * @return .
	 */
	@GetMapping("/public/products/{field}/{type}")
	public ResponseEntity<List<ProductDTO>> getProductsSort(@PathVariable String field, @PathVariable String type) {
		return ResponseEntity.ok().body(productService.getProductsSort(field, type));
	}

	@GetMapping("/public/products")
	public ResponseEntity<List<ProductDTO>> getAllProduct(Pageable pageable) {
		Page<ProductDTO> page = this.productService.findAll(pageable);
		return ResponseEntity.ok().body(page.getContent());
	}
	
	
	@DeleteMapping("/public/products/{id}")
	public ResponseEntity<Long> delete(@PathVariable Long id) {
		productService.delete(id);
		return ResponseEntity.ok().body(id);
	}
	/**
	 * Search X number of products from a field
	 * 
	 * @param field          the field of research
	 * @param numberOfResult the number of products wanted
	 * @return a page of X products
	 * 
	 * @author Cecile
	 */
	@GetMapping("/public/products/random/{field}/{numberOfResult}")
	public ResponseEntity<List<ProductDTO>> getRandomProducts(@PathVariable String field,
			@PathVariable int numberOfResult) {
		System.out.println("BLOP-1");
		Page<ProductDTO> page = this.productService.findRandomProducts(field, numberOfResult);
		if (page == null) {
			System.out.println("BLOP-2");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		System.out.println("BLOP-3");
		return ResponseEntity.ok().body(page.getContent());
	}

	@GetMapping("/public/products/count")
	public ResponseEntity<Integer> ProductCount() {
		return ResponseEntity.ok().body(productService.productCounter());
	}

	/**
	 * @param productSearchCriteria model for criterias filters
	 * @return List of products
	 * @author Claire/Luca
	 */
	@PostMapping("/public/products/search/") /* get?? */
	public ResponseEntity<ResponseSearchCriteria> getProduct(@RequestBody ProductSearchCriteria productSearchCriteria)
			throws JsonProcessingException {

		Pageable pageable = PageRequest.of(productSearchCriteria.getPage(), productSearchCriteria.getPageSize());

		Page<ProductDAO> page = productService.getProductCriteria(productSearchCriteria, pageable);
		ResponseSearchCriteria newRes = new ResponseSearchCriteria(page.getTotalElements(), page.getContent());

		return ResponseEntity.ok().body(newRes);

	}

}
