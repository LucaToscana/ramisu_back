package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.TagDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagDAO, Long> {
}
