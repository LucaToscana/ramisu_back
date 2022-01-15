package com.m2i.warhammermarket.repository;
import com.m2i.warhammermarket.entity.DAO.InhabitDAO;
import com.m2i.warhammermarket.entity.DAO.InhabitId;

import org.springframework.data.jpa.repository.JpaRepository;


public interface InhabitRepository  extends JpaRepository<InhabitDAO, InhabitId> {

}

