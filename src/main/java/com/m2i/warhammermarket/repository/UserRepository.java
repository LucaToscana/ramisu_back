package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.AuthorityDAO;
import com.m2i.warhammermarket.entity.DAO.UserDAO;
import com.m2i.warhammermarket.security.AuthorityConstant;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDAO, Long> {

    UserDAO findByMail(String mail);

    UserDAO findByToken(String passwordResetToken);
   
    List<UserDAO> findByAuthorities_Name(String roles);

   // List<UserDAO> findByRolesIn(Set<AuthorityDAO> roles);

}
