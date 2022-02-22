package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.ProductDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import javax.transaction.Transactional;


public interface ProductRepository extends JpaRepository<ProductDAO, Long> {

    @Query("SELECT COUNT(p) FROM ProductDAO p")
    Integer countProduct();

    @Query("from ProductDAO p order by p.label")
    List<ProductDAO> getProductsSortByLabelAsc();

    @Query("from ProductDAO p order by p.label desc")
    List<ProductDAO> getProductsSortByLabelDesc();

    @Query("from ProductDAO p order by p.price")
    List<ProductDAO> getProductsSortByPriceAsc();

    @Query("from ProductDAO p order by p.price desc")
    List<ProductDAO> getProductsSortByPriceDesc();
   
    
    /**
     * Get random products in a page
     * 
     * @param firstPageWithXElements
     * @return a Page containing the returned products
     * @author Cecile
     */
    @Query("FROM ProductDAO p order by function('RAND')")
    Page<ProductDAO> getRandomProducts(Pageable firstPageWithXElements);
    
    /**
     * Get random products in a page, ordered by promotion
     * 
     * @param firstPageWithXElements
     * @return a Page containing the returned products
     * @author Cecile
     */
    @Query("FROM ProductDAO p order by p.promotion desc")
    Page<ProductDAO> getRandomPromoProducts(Pageable firstPageWithXElements);
    
    /**
     * Get random products in a page, ordered by year of production
     * 
     * @param firstPageWithXElements
     * @return a Page containing the returned products
     * @author Cecile
     */
    @Query("FROM ProductDAO p order by p.yearOfProduction desc")
    Page<ProductDAO> getRandomTopSaleProducts(Pageable firstPageWithXElements);

    
    
    @Modifying
	void deleteById(Long id);

}
