package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.ProductDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
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
}
