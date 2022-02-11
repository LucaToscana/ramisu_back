package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.OrderDAO;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderDAO, Long> {

    List<OrderDAO> findAllByUserUserId(Long id);

	List<OrderDAO> findAllByUserUserId(Long id, Sort by);

}
