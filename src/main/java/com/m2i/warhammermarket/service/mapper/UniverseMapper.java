package com.m2i.warhammermarket.service.mapper;

import com.m2i.warhammermarket.entity.DAO.UniverseDAO;
import com.m2i.warhammermarket.entity.DTO.UniverseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UniverseMapper {

    public List<UniverseDTO> universesToUniversesDTOList(List<UniverseDAO> universeDAOS) {
        return universeDAOS.stream()
                .filter(Objects::nonNull)
                .map(this::universeDAOToUniverseDTO)
                .collect(Collectors.toList());
    }

    public UniverseDTO universeDAOToUniverseDTO(UniverseDAO universeDAO) {
        return new UniverseDTO(universeDAO);
    }

    public List<UniverseDAO> universeDTOsToUniverses(List<UniverseDTO> universeDTOs) {
        return universeDTOs.stream()
                .filter(Objects::nonNull)
                .map(this::universeDTOToUniverseDAO)
                .collect(Collectors.toList());
    }

    public UniverseDAO universeDTOToUniverseDAO(UniverseDTO universeDTO) {
        if (universeDTO == null) {
            return null;
        } else {
            UniverseDAO universeDAO = new UniverseDAO();
            universeDAO.setId(universeDTO.getId());
            universeDAO.setRefCode(universeDTO.getRefCode());
            universeDAO.setLabel(universeDTO.getLabel());
            return universeDAO;
        }
    }

    public UniverseDAO universeFromId(Long id) {
        if (id == null) {
            return null;
        }
        UniverseDAO universeDAO = new UniverseDAO();
        universeDAO.setId(id);
        return universeDAO;
    }

}
