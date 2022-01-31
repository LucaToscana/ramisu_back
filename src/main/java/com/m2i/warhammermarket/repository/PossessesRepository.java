package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.PossessesDAO;
import com.m2i.warhammermarket.entity.DAO.PossessesId;
import com.m2i.warhammermarket.entity.DAO.ProductDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PossessesRepository extends JpaRepository<PossessesDAO, PossessesId> {


}
