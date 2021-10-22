package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
