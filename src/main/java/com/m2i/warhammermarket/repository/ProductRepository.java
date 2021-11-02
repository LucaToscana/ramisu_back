package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.Product;
import com.m2i.warhammermarket.entity.DAO.User;
import com.m2i.warhammermarket.entity.DTO.ProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("from Product p order by p.label")
    List<Product> getProductsSortByLabelAsc();

    @Query("from Product p order by p.label desc")
    List<Product> getProductsSortByLabelDesc();

    @Query("from Product p order by p.price")
    List<Product> getProductsSortByPriceAsc();

    @Query("from Product p order by p.price desc")
    List<Product> getProductsSortByPriceDesc();
}
