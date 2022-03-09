package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.entity.DAO.UniverseDAO;
import com.m2i.warhammermarket.entity.DTO.UniverseDTO;
import com.m2i.warhammermarket.repository.UniverseRepository;
import com.m2i.warhammermarket.service.UniverseService;
import org.modelmapper.ModelMapper;
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
    private ModelMapper modelMapper;

    public UniverseServiceImplement() {

    }

    @Override
    public Optional<UniverseDTO> save(Long id) {
        return Optional.ofNullable(modelMapper.map(this.universeRepository.findById(id), UniverseDTO.class));
    }

    @Override
    public List<UniverseDTO> findAll() {
        return this.universeRepository.findAll().stream().map(e->modelMapper.map(e, UniverseDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Optional<UniverseDTO> findOne(Long id) {
//        return Optional.ofNullable(this.modelMapper.universeToUniverseDTO(this.universeRepository.findById(id).orElse(null)));
        return Optional.ofNullable(modelMapper.map(this.universeRepository.findById(id), UniverseDTO.class));
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<UniverseDAO> save(UniverseDTO universeDTO) {
        return null;
    }

    @Override
    public List<UniverseDAO> save(UniverseDAO universeDAO) { return null; }
}
