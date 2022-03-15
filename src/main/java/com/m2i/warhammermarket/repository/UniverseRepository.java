package com.m2i.warhammermarket.repository;

import com.m2i.warhammermarket.entity.DAO.UniverseDAO;
import com.m2i.warhammermarket.entity.DTO.UniverseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniverseRepository extends JpaRepository<UniverseDAO, Long> {
    List<UniverseDTO> findById(UniverseDTO universe);
}
