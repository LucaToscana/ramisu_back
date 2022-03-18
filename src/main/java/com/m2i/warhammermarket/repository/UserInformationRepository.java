package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.UserDAO;
import com.m2i.warhammermarket.entity.DAO.UsersInformationDAO;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInformationRepository extends JpaRepository<UsersInformationDAO, Long> {
    @Query("select u from UsersInformationDAO u " +
            "join UserDAO us on us.id = u.user.id " +
            "where us.mail = :login")
    UsersInformationDAO getByMail(@Param("login") String login);

	UsersInformationDAO findByUser(UserDAO user);

	UsersInformationDAO findByUser(UserDAO user, Sort by);
}
