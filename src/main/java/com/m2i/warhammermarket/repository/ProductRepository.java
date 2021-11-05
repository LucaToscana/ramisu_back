package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.Product;
import com.m2i.warhammermarket.entity.DAO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT COUNT(p) FROM Product p")
    Integer countProduct();

}
