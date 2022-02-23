package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.entity.DAO.CategoryDAO;
import com.m2i.warhammermarket.entity.DAO.ProductDAO;
import com.m2i.warhammermarket.entity.DAO.UniverseDAO;
import com.m2i.warhammermarket.entity.DTO.ProductDTO;
import com.m2i.warhammermarket.entity.wrapper.ProductAddWrapper;
import com.m2i.warhammermarket.model.ProductRequestModel;
import com.m2i.warhammermarket.model.ProductSearchCriteria;
import com.m2i.warhammermarket.repository.CategoryRepository;
import com.m2i.warhammermarket.repository.ProductRepository;
import com.m2i.warhammermarket.repository.ProductRepositoryCriteria;
import com.m2i.warhammermarket.repository.UniverseRepository;
import com.m2i.warhammermarket.service.ProductService;
import com.m2i.warhammermarket.service.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
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
    @Autowired
    private UniverseRepository universeRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private EntityManager entityManager;

    //Claire
    private ProductSearchCriteria productSearchCriteria;
    @Autowired
    private ProductRepositoryCriteria productRepositoryCriteria;
    //-

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
    Page<ProductDAO> productPageDAO = null;
    Page<ProductDTO> productPageDTO = null;
	@Override
	public Page<ProductDTO> findRandomProducts(String field, int numberOfResult) {
		Pageable firstPageWithXElements = PageRequest.of(0, numberOfResult);

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


    /**
     * @param productSearchCriteria
     *  This method is used for 'Dynamic Search'.
     *  Means PersonSearchRequestModel has four variables, based on any combination of variables a search will happen.
     * @author Claire
     */
    public List<ProductDAO> getProductCriteria(ProductSearchCriteria productSearchCriteria){
        return productRepositoryCriteria.findAllWithFilters(productSearchCriteria);
    }
    
    /**
     * @param productWrapper
     *  This method is used for get from a front a complete product object and to register this object in database.
     *  @return a product
     * @author Amal
     */ 
    @Override
    public ProductDAO saveProduct(ProductAddWrapper productWrapper) {
    	
    	ProductDAO product = new ProductDAO();
    	
    	product.setEan13(productWrapper.getEan13());
    	product.setDescription(productWrapper.getDescription());
    	product.setLabel(productWrapper.getLabelProduct());
    	product.setPrice(productWrapper.getPrice());
    	product.setPromotion(productWrapper.getPromotion());
    	product.setStock(productWrapper.getStock());
    	product.setPicture(productWrapper.getPicture());
    	
    	
    	
    	UniverseDAO universe = universeRepository.findById(productWrapper.getIdUniverse()).get();
    	
    	//universe.setId(productWrapper.getIdUniverse());
    	//universe.setLabel(productWrapper.getLabelCategory());
    	////universe.setRefCode(productWrapper.getRefCodeUniverse());
    	
    	//universeRepository.save(universe); 
    	product.setUniverse(universe);

    	
    	CategoryDAO category = categoryRepository.findById(productWrapper.getIdCategory()).get();
    	//category.setId(productWrapper.getIdCategory());
    	//category.setLabel(productWrapper.getLabelCategory());
    	//category.setRefCode(productWrapper.getRefCodeCategory());

    	//categoryRepository.save(category);
    	product.setCategorie(category);
    	
    	
    	productRepository.save(product);
    	
    	
        return product ;
    }
    
    /**
     * @param Long id and productWrapper
     *  This method is used to update a product
     *  @return a product updated
     * @author Amal
     */ 
    
    @Override    
    public ProductDAO updateProduct(ProductAddWrapper productWrapper) {
	
	
    	ProductDAO product = productRepository.findById(productWrapper.getIdProduct()).get();
    	
    	
    	product.setId(productWrapper.getIdProduct());
    	product.setEan13(productWrapper.getEan13());
    	product.setDescription(productWrapper.getDescription());
    	product.setLabel(productWrapper.getLabelProduct());
    	product.setPrice(productWrapper.getPrice());
    	product.setPromotion(productWrapper.getPromotion());
    	product.setStock(productWrapper.getStock());
    	product.setPicture(productWrapper.getPicture());
    	
    	
    	
    	UniverseDAO universe = universeRepository.findById(productWrapper.getIdUniverse()).get();
    	
    	//UniverseDAO universe = new UniverseDAO();
    	//universe.setId(productWrapper.getIdUniverse());
    	//universe.setLabel(productWrapper.getLabelUniverse());
    	//universe.setRefCode(productWrapper.getRefCodeUniverse());
    	
    	//universeRepository.saveAndFlush(universe);
    	product.setUniverse(universe);
    	

    	CategoryDAO category = categoryRepository.findById(productWrapper.getIdCategory()).get();
    	//CategoryDAO category = new CategoryDAO();
    	////category.setLabel(productWrapper.getLabelCategory());
    	//category.setRefCode(productWrapper.getRefCodeCategory());
    	
    	//categoryRepository.saveAndFlush(category);
    	product.setCategorie(category);
    	
    	
    	
    	productRepository.saveAndFlush(product);
    	
    	
        return product ;
    }

/**
 * @param Long id of product
 *  This method delete a product on database
 * @author Amal
 */ 
    
   @Override 
    public void deleteProduct(Long id) {
	   	ProductDAO idProduct = productRepository.getById(id);
	   	productRepository.delete(idProduct);
        
    }
 
    
}
