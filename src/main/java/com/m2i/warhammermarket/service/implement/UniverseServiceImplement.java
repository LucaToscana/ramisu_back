package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.entity.DTO.UniverseDTO;
import com.m2i.warhammermarket.repository.UniverseRepository;
import com.m2i.warhammermarket.service.UniverseService;
import com.m2i.warhammermarket.service.mapper.UniverseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UniverseServiceImplement implements UniverseService {

    @Autowired
    private UniverseRepository universeRepository;

    @Autowired
    private UniverseMapper universeMapper;

    public UniverseServiceImplement() {

    }

    @Override
    public UniverseDTO save(UniverseDTO universe) {
        return null;
    }

    @Override
    public List<UniverseDTO> findAll() {
        return this.universeRepository.findAll().stream().map(universeMapper::universeToUniverseDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<UniverseDTO> findOne(Long id) {
        return Optional.ofNullable(this.universeMapper.universeToUniverseDTO(this.universeRepository.findById(id).orElse(null)));
    }

    @Override
    public void delete(Long id) {

    }
}
