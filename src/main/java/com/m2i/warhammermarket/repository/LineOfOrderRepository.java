package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.LineOfOrderDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineOfOrderRepository extends JpaRepository<LineOfOrderDAO, Long> {

    List<LineOfOrderDAO> findAllByIdIdOrder(Long idOrder);
}
