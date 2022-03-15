package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.ChatMessageDAO;
import com.m2i.warhammermarket.entity.DAO.ChatMessageId;
import com.m2i.warhammermarket.entity.DAO.ChatsDAO;
import com.m2i.warhammermarket.entity.DAO.UsersInformationDAO;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatsRepository  extends JpaRepository<ChatsDAO,Long> {

	List<ChatsDAO> findByUserCustomers(UsersInformationDAO userInfo);

	List<ChatsDAO> findByUserCustomers(UsersInformationDAO userInfo, Sort by);

	
	


}

