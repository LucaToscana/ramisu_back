package com.m2i.warhammermarket.service;

import com.m2i.warhammermarket.entity.DAO.UniverseDAO;
import com.m2i.warhammermarket.entity.DTO.UniverseDTO;

import java.util.List;
import java.util.Optional;

public interface UniverseService {

    Optional<UniverseDTO> save(Long id);

    List<UniverseDTO> findAll();

    Optional<UniverseDTO> findOne(Long id);

    void delete(Long id);

    List<UniverseDAO> save(UniverseDTO universeDTO);

    List<UniverseDAO> save(UniverseDAO universeDAO);
}
