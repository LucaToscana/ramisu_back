package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.UniverseDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UniverseRepository extends JpaRepository<UniverseDAO, Long> {
    UniverseDAO postLabelUniverse(@Param("idUniverse") Long idUniverse);
}
