package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.ProductDAO;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;

/**
 * This is a Repository for product research with Criteria
 * @since 1.1
 * @author Claire
 */

public class ProductRepositoryCriteria {

    private final EntityManager entityManager ;
    private final CriteriaBuilder criteriaBuilder;

    public ProductRepositoryCriteria (EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<ProductDAO> searchAllWithFilters() {

    }
}
