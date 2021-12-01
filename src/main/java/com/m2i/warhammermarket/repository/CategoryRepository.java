package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.CategoryDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryDAO, Long> {
}
