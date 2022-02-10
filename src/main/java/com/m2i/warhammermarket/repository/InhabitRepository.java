package com.m2i.warhammermarket.repository;
import com.m2i.warhammermarket.entity.DAO.AddressDAO;
import com.m2i.warhammermarket.entity.DAO.InhabitDAO;
import com.m2i.warhammermarket.entity.DAO.InhabitId;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;


public interface InhabitRepository  extends JpaRepository<InhabitDAO, InhabitId> {
	
	
	Set<InhabitDAO> findAllByUserUserId(Long id);


}

