package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.entity.DAO.UniverseDAO;
import com.m2i.warhammermarket.entity.DTO.UniverseDTO;
import com.m2i.warhammermarket.repository.UniverseRepository;
import com.m2i.warhammermarket.service.UniverseService;
import com.m2i.warhammermarket.service.mapper.UniverseMapper;
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

    @Autowired
    private UniverseMapper universeMapper;

    private UniverseDAO universeDAO;

    public UniverseServiceImplement() {

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

    /**
     * @param universeDTO object
     *  This method is used to create a new universe for the administrator
     *  @return universe
     * @author Brice
     *//*
    @Override
    public UniverseDAO saveUniverseDTO(UniverseDTO universeDTO) {

        UniverseDAO universe= new UniverseDAO();
        universe.setId(universeMapper.getIdUniverse());
        universe.setLabel(universeMapper.getLabelUniverse());
        universe.setRefCode(universeMapper.getRefCode());

        //here we are checking if the category already exist by his name(label)
        String label=universe.getLabel();

        if(universeRepository.findByLabel(label).isPresent()) {
            return null;
        }

        return universeRepository.save(universe);
    }*/
    @Override
    public UniverseDTO saveUniverseDTO(UniverseDTO universeDTO) {
        //return modelMapper.map(universeRepository.save(modelMapper.map(universeDTO, universeDAO)), universeDAO);
        System.out.println(universeDTO + "1");
        UniverseDAO universe = universeMapper.universeDTOToUniverse(universeDTO);
        System.out.println(universeDTO + "2" + universe);
        UniverseDAO universeSave = universeRepository.save(universe);
        System.out.println(universeDTO + "3");
        UniverseDTO universeResult = universeMapper.universeDAOToUniverseDTO(universeSave);
        System.out.println(universeDTO + "4");
        return universeResult;
    }
}
