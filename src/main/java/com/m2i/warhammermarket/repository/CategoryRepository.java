package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.CategoryDAO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryDAO, Long> {
	
	/**
	 * @author Amal
	 * ICI ON RECHERCHE UNE CATEGORY PAR NOM.
	 * @param 
	 * @return id
	 */
	public Optional<CategoryDAO> findByLabel(String label);
}
