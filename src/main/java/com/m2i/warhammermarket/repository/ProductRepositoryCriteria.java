package com.m2i.warhammermarket.repository;

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

   /* public Page<ProductDAO> searchAllWithFilters(ProductRequestModel productRequestModel,
                                                 ProductRepositoryCriteria productRepositoryCriteria) {
        CriteriaQuery<ProductDAO> criteriaQuery = criteriaBuilder.createQuery(ProductDAO.class);
        Root<ProductDAO> productDAORoot = criteriaQuery.from(ProductDAO.class );
        Predicate predicate = getPredicate (productRepositoryCriteria, productDAORoot);
        criteriaQuery.where(predicate);
        setOrder(productRequestMode, criteriaQuery, productDAORoot);

        TypedQuery<ProductDAO> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(productRequestModel.getPageNumber() * productRequestModel.getPageSize());
        typedQuery.setMaxResults(productRequestModel.getPageSize());

        Pageable pageable = getPageable(productRequestModel);

        long productCount = getProductCount (predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, productCount);
    }*/

}
