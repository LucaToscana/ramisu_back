package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.CategoryDAO;
import com.m2i.warhammermarket.entity.DAO.UniverseDAO;
import com.m2i.warhammermarket.entity.DTO.UniverseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniverseRepository extends JpaRepository<UniverseDAO, Long> {
    // UniverseDAO postLabelUniverse(@Param("id") Long idUniverse);

    List<CategoryDAO> findById(UniverseDTO universe);
}
