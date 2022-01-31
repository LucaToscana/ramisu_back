package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.ProductDAO;
import com.m2i.warhammermarket.model.ProductRequestModel;
import com.m2i.warhammermarket.model.ProductSearchCriteria;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This is a Repository for product research with Criteria
 * 
 * @since 1.1
 * @author Claire
 */
@Repository
public class ProductRepositoryCriteria {

	private final EntityManager em;
	private final CriteriaBuilder cb;

	public ProductRepositoryCriteria(EntityManager manager) {
		this.em = manager;
		this.cb = manager.getCriteriaBuilder();
	}

	public Page<ProductDAO> findAllWithFilters(ProductSearchCriteria searchCriteria, Pageable pageable) {
		CriteriaQuery<ProductDAO> ctQuery = cb.createQuery(ProductDAO.class);
		Root<ProductDAO> productDAORoot = ctQuery.from(ProductDAO.class);
		Predicate predicate = getPredicate(searchCriteria, productDAORoot);
		ctQuery.where(predicate);
		ctQuery.distinct(true);

		ctQuery.orderBy(cb.asc(productDAORoot.get("label")));

		TypedQuery<ProductDAO> typedQuery = em.createQuery(ctQuery);
		Long total = (long) typedQuery.getResultList().size();

		/*
		 * 
		 * // Define the CriteriaQuery CriteriaBuilder cb = em.getCriteriaBuilder();
		 * CriteriaQuery<Book> cq = cb.createQuery(Book.class); Root<Book> root =
		 * cq.from(Book.class); cq.orderBy(cb.asc(root.get(Book_.title)))
		 */

		int pageNumber = pageable.getPageNumber();
		int pageSize = pageable.getPageSize();
		typedQuery.setFirstResult((pageNumber) * pageSize);
		typedQuery.setMaxResults(pageSize);

		List<ProductDAO> result = typedQuery.getResultList();
		Page<ProductDAO> resultP = new PageImpl<ProductDAO>(result, pageable, total);

		return resultP;
	}

//    public Page<ProductDAO> findAllWithFiltersTemp (ProductRequestModel productRM,
//                                                ProductSearchCriteria searchCriteria) {
//        CriteriaQuery<ProductDAO> ctQuery = ct.createQuery(ProductDAO.class);
//        Root<ProductDAO> productDAORoot = ctQuery.from(ProductDAO.class );
//        Predicate predicate = getPredicate (searchCriteria, productDAORoot);
//        ctQuery.where(predicate);
//        setOrder(productRM, ctQuery, productDAORoot);
//
//        TypedQuery<ProductDAO> typedQuery = em.createQuery(ctQuery);
//        typedQuery.setFirstResult(productRM.getPageNumber() * productRM.getPageSize());
//        typedQuery.setMaxResults(productRM.getPageSize());
//
//        Pageable pageable = getPageable(productRM);
//
//        long productCount = getProductCount (predicate);
//
//        return new PageImpl<>(typedQuery.getResultList(), pageable, productCount);
//    }

	private Predicate getPredicate(ProductSearchCriteria productSearchCriteria, Root<ProductDAO> productDAORoot) {
		List<Predicate> predicates = new ArrayList<>();
		if (Objects.nonNull(productSearchCriteria.getLabel())) {
			predicates.add(cb.like(productDAORoot.get("label"),

					"%" + productSearchCriteria.getLabel() + "%"));
		}

		if (Objects.nonNull(productSearchCriteria.getUniverse()) && productSearchCriteria.getUniverse().size() > 0) {
			Stream<Predicate> p = productSearchCriteria.getUniverse().stream()
					.map(universe -> cb.like(productDAORoot.get("universe").get("label"), "%" + universe + "%"));
			List<Predicate> predicateList = p.collect(Collectors.toList());
			predicates.add(cb.or(predicateList.toArray(new Predicate[0])));
		}

		if (Objects.nonNull(productSearchCriteria.getCategory()) && productSearchCriteria.getCategory().size() > 0) {
			Stream<Predicate> p = productSearchCriteria.getCategory().stream()
					.map(categorie -> cb.like(productDAORoot.get("categorie").get("label")

							, "%" + categorie + "%"));
			List<Predicate> predicateList = p.collect(Collectors.toList());
			predicates.add(cb.or(predicateList.toArray(new Predicate[0])));
		}

		if (Objects.nonNull(productSearchCriteria.getTag()) && productSearchCriteria.getTag().size() > 0) {

			Join join = productDAORoot.join("possessesTagsProduct").join("tag");
			// cb.equal(join.get("id"),authorId);
			Stream<Predicate> p = productSearchCriteria.getTag().stream().map(tag -> cb.like(join.get("label")

					, "%" + tag + "%"));

			List<Predicate> predicateList = p.collect(Collectors.toList());
			predicates.add(cb.or(predicateList.toArray(new Predicate[0])));

		}

		if (Objects.nonNull(productSearchCriteria.getMinPrice())
				&& Objects.nonNull(productSearchCriteria.getMaxPrice())) {

			Predicate onStart = cb.greaterThanOrEqualTo(productDAORoot.get("price"),
					productSearchCriteria.getMinPrice());
			Predicate onEnd = cb.lessThanOrEqualTo(productDAORoot.get("price"), productSearchCriteria.getMaxPrice());

			predicates.add(onStart);
			predicates.add(onEnd);

		}

		return cb.and(predicates.toArray(new Predicate[0]));
	}

	private void setOrder(ProductRequestModel productRM, CriteriaQuery<ProductDAO> criteriaQuery,
			Root<ProductDAO> productDAORoot) {
		if (productRM.getSortDirection().equals(Sort.Direction.ASC)) {
			criteriaQuery.orderBy(cb.asc(productDAORoot.get(productRM.getSortBy())));
		} else {
			criteriaQuery.orderBy(cb.desc(productDAORoot.get(productRM.getSortBy())));
		}
	}

	private Pageable getPageable(ProductRequestModel productRequestModel) {
		Sort sort = Sort.by(productRequestModel.getSortDirection(), productRequestModel.getSortBy());
		return PageRequest.of(productRequestModel.getPageNumber(), productRequestModel.getPageSize(), sort);
	}

	private long getProductCount(Predicate predicate) {
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<ProductDAO> countRoot = countQuery.from(ProductDAO.class);
		countQuery.select(cb.count(countRoot)).where(predicate);
		return em.createQuery(countQuery).getSingleResult();
	}
}
