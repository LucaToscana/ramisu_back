package com.m2i.warhammermarket.service;

import com.m2i.warhammermarket.entity.DTO.UniverseDTO;

import java.util.List;

public interface UniverseService {

    List<UniverseDTO> findAll();

    UniverseDTO findOne(Long id);

    void delete(Long id);

    UniverseDTO saveUniverseDTO(UniverseDTO universeDTO);
}
