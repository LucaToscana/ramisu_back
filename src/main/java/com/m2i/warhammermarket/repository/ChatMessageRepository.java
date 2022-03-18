package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.ChatMessageDAO;
import com.m2i.warhammermarket.entity.DAO.ChatMessageId;


import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatMessageRepository  extends JpaRepository<ChatMessageDAO,ChatMessageId> {
	
	
	Set<ChatMessageDAO> findAllByUserUserId(Long id);


}

