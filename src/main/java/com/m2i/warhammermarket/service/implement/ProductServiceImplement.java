package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.entity.DAO.ProductDAO;
import com.m2i.warhammermarket.entity.DTO.ProductDTO;
import com.m2i.warhammermarket.repository.ProductRepository;
import com.m2i.warhammermarket.service.ProductService;
import com.m2i.warhammermarket.service.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImplement implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductRepository productRepository;

    public ProductServiceImplement() {
    }

    @Override
    public ProductDTO save(ProductDTO product) {
        return null;
    }

    @Override
    public Page<ProductDTO> findAll(Pageable pageable) {
        return this.productRepository.findAll(pageable).map(this.productMapper::productToProductDTO);
    }

    @Override
    public Optional<ProductDTO> findOne(Long id) {
        return Optional.ofNullable(this.productMapper.productToProductDTO(this.productRepository.findById(id).orElse(null)));
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<ProductDTO> getProductsSort(String field, String type) {
        if(field.equalsIgnoreCase("label") && type.equalsIgnoreCase("desc"))
            return productRepository.getProductsSortByLabelDesc().stream().map(productMapper::productToProductDTO).collect(Collectors.toList());
        else if(field.equalsIgnoreCase("price") && type.equalsIgnoreCase("asc"))
            return productRepository.getProductsSortByPriceAsc().stream().map(productMapper::productToProductDTO).collect(Collectors.toList());
        else if (field.equalsIgnoreCase("price") && type.equalsIgnoreCase("desc"))
            return productRepository.getProductsSortByPriceDesc().stream().map(productMapper::productToProductDTO).collect(Collectors.toList());
        return productRepository.getProductsSortByLabelAsc().stream().map(productMapper::productToProductDTO).collect(Collectors.toList());

    }

    @Override
    public Integer productCounter() {
        return productRepository.countProduct();
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
	@Override
	public Page<ProductDTO> findRandomProducts(String field, int numberOfResult) {
		Pageable firstPageWithXElements = PageRequest.of(0, numberOfResult);
		Page<ProductDAO> productPageDAO = null;
		Page<ProductDTO> productPageDTO = null;
		switch (field.toLowerCase()) {

			case "random" :
				productPageDAO = this.productRepository.getRandomProducts(firstPageWithXElements);
				
				if (productPageDAO.hasContent()) {
					productPageDTO = productPageDAO.map(this.productMapper::productToProductDTO);
				}
				break;

			case "promotion" :
				productPageDAO = this.productRepository.getRandomPromoProducts(firstPageWithXElements);
				
				if (productPageDAO.hasContent()) {
					productPageDTO = productPageDAO.map(this.productMapper::productToProductDTO);
				}
				break;

			case "topsale" :
				productPageDAO = this.productRepository.getRandomTopSaleProducts(firstPageWithXElements);
					
				if (productPageDAO.hasContent()) {
					productPageDTO = productPageDAO.map(this.productMapper::productToProductDTO);
				}
				break;

			default : productPageDTO = null;
		}
		return productPageDTO;
	}
}
