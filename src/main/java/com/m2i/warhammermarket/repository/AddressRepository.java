package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.AddressDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressDAO, Long> {
    @Query("select a from AddressDAO a " +
            "join InhabitDAO i on i.id.idAddress = a.id " +
            "where i.id.idUser = :idUser and i.isMain = 1")
    AddressDAO getAddressMainByIdUser(@Param("idUser") Long idUser);
}
