package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.NotificationDAO;
import com.m2i.warhammermarket.entity.DAO.NotificationId;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository  extends JpaRepository<NotificationDAO,NotificationId> {
	
	
	Set<NotificationDAO> findAllByUserUserId(Long id);


}

