package com.m2i.warhammermarket.service;

import com.m2i.warhammermarket.entity.DTO.UniverseDTO;

import java.util.List;
import java.util.Optional;

public interface UniverseService {

    List<UniverseDTO> findAll();

    Optional<UniverseDTO> findOne(Long id);

    void delete(Long id);

    UniverseDTO saveUniverseDTO(UniverseDTO universeDTO);
}
