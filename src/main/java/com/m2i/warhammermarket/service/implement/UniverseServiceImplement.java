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

import static org.springframework.data.mapping.Alias.ofNullable;

@Service
@Transactional
public class UniverseServiceImplement implements UniverseService {

    @Autowired
    private UniverseRepository universeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UniverseMapper universeMapper;

    public UniverseServiceImplement() {

    }

    @Override
    public List<UniverseDTO> findAll() {
        return this.universeRepository.findAll().stream().map(e->modelMapper.map(e, UniverseDTO.class)).collect(Collectors.toList());
    }

    @Override
    public UniverseDTO findOne(Long id) {
        // return Optional.ofNullable(this.modelMapper.universeToUniverseDTO(this.universeRepository.findById(id).orElse(null)));
        return modelMapper.map(this.universeRepository.findById(id).orElse(null), UniverseDTO.class);
    }

    /**
     * @param universeDTO object
     *  This method is used to create a new universe from the administrator
     *  @return universe
     * @author Brice
     */
    @Override
    public UniverseDTO saveUniverseDTO(UniverseDTO universeDTO) {
        // Instantiates the recovered object in the DAO to attach the attributes/fields to it
        UniverseDAO universe = universeMapper.universeDTOToUniverseDAO(universeDTO);
        // Use Hibernate.SQL to prepare insert into table
        UniverseDAO universeSave = universeRepository.save(universe);
        //
        UniverseDTO universeResult = universeMapper.universeDAOToUniverseDTO(universeSave);
        // Return the result
        return universeResult;
    }

    /**
     * @param id object
     *  This method is used to delete a universe from the administrator
     * @author Brice
     */
    @Override
    public void deleteUniverseDTO(Long id) {
        // get id
        // UniverseDAO universeDAO = universeRepository.findById(universeDTO);
        // delete by id
        // update universes
    }
}
