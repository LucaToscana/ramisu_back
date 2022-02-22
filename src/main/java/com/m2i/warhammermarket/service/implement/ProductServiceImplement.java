package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.entity.DAO.PossessesDAO;
import com.m2i.warhammermarket.entity.DAO.ProductDAO;
import com.m2i.warhammermarket.entity.DAO.TagDAO;
import com.m2i.warhammermarket.entity.DTO.ProductDTO;
import com.m2i.warhammermarket.model.ProductRequestModel;
import com.m2i.warhammermarket.model.ProductSearchCriteria;
import com.m2i.warhammermarket.model.ResponseProductDetails;
import com.m2i.warhammermarket.repository.ProductRepository;
import com.m2i.warhammermarket.repository.ProductRepositoryCriteria;
import com.m2i.warhammermarket.service.ProductService;
import com.m2i.warhammermarket.service.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImplement implements ProductService {

	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private EntityManager entityManager;

	// Claire
	private ProductSearchCriteria productSearchCriteria;
	@Autowired
	private ProductRepositoryCriteria productRepositoryCriteria;
	// -

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
		return Optional
				.ofNullable(this.productMapper.productToProductDTO(this.productRepository.findById(id).orElse(null)));
	}

	@Override
	public void delete1(Long id) {

	}

	@Override
	public List<ProductDTO> getProductsSort(String field, String type) {
		if (field.equalsIgnoreCase("label") && type.equalsIgnoreCase("desc"))
			return productRepository.getProductsSortByLabelDesc().stream().map(productMapper::productToProductDTO)
					.collect(Collectors.toList());
		else if (field.equalsIgnoreCase("price") && type.equalsIgnoreCase("asc"))
			return productRepository.getProductsSortByPriceAsc().stream().map(productMapper::productToProductDTO)
					.collect(Collectors.toList());
		else if (field.equalsIgnoreCase("price") && type.equalsIgnoreCase("desc"))
			return productRepository.getProductsSortByPriceDesc().stream().map(productMapper::productToProductDTO)
					.collect(Collectors.toList());
		return productRepository.getProductsSortByLabelAsc().stream().map(productMapper::productToProductDTO)
				.collect(Collectors.toList());

	}

	@Override
	public Integer productCounter() {
		return productRepository.countProduct();
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
	Page<ProductDAO> productPageDAO = null;
	Page<ProductDTO> productPageDTO = null;

	@Override
	public Page<ProductDTO> findRandomProducts(String field, int numberOfResult) {
		Pageable firstPageWithXElements = PageRequest.of(0, numberOfResult);

		switch (field.toLowerCase()) {

		case "random":
			productPageDAO = this.productRepository.getRandomProducts(firstPageWithXElements);

			if (productPageDAO.hasContent()) {
				productPageDTO = productPageDAO.map(this.productMapper::productToProductDTO);
			}
			break;

		case "promotion":
			productPageDAO = this.productRepository.getRandomPromoProducts(firstPageWithXElements);

			if (productPageDAO.hasContent()) {
				productPageDTO = productPageDAO.map(this.productMapper::productToProductDTO);
			}
			break;

		case "topsale":
			productPageDAO = this.productRepository.getRandomTopSaleProducts(firstPageWithXElements);

			if (productPageDAO.hasContent()) {
				productPageDTO = productPageDAO.map(this.productMapper::productToProductDTO);
			}
			break;

		default:
			productPageDTO = null;
		}
		return productPageDTO;
	}

	/**
	 * @param productSearchCriteria This method is used for 'Dynamic Search'. Means
	 *                              PersonSearchRequestModel has four variables,
	 *                              based on any combination of variables a search
	 *                              will happen.
	 * @author Claire
	 */
	public Page<ProductDAO> getProductCriteria(ProductSearchCriteria productSearchCriteria, Pageable pageable) {
		return productRepositoryCriteria.findAllWithFilters(productSearchCriteria, pageable);
	}

	@Override
	public Optional<ResponseProductDetails> findOneDetails(Long id) {
		ProductDAO p = this.productRepository.findById(id).orElse(null);
		/*
		 * When there is no encounter order, it returns any element from the Stream.
		 * According to the java.util.streams package documentation,
		 *  “Streams may or may
		 * not have a defined encounter order. I t depends on the source and the
		 * intermediate operations.”
		 */

		Optional<TagDAO> tag = Optional.ofNullable(p.getPossessesTagsProduct().stream().findFirst().get().getTag());

		Set<PossessesDAO> list = tag.get().getPossessesProductsTag();
		Optional<ProductDTO> product = Optional.ofNullable(this.productMapper.productToProductDTO(p));
		List<ProductDTO> productsRelated = new ArrayList<ProductDTO>();
		for (PossessesDAO prod : list) {
			if (prod.getProduct().getId() != id) {
				productsRelated.add(this.productMapper.productToProductDTO(prod.getProduct()));
			}
		}

		Optional<ResponseProductDetails> response = Optional
				.ofNullable(new ResponseProductDetails(product.get(), productsRelated, 0));

		return response;
	}

	@Override
	public void delete(Long id) {
		
		System.out.println("ciao");
		ProductDAO p = 	productRepository.getById(id);
		System.out.println(p);
    productRepository.delete(p);		
	ProductDAO p2 = 	productRepository.getById(id);
System.out.println(p2);
    
	}

}
