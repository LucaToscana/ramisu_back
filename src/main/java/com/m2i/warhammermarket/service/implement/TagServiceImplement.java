package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.entity.DTO.TagDTO;
import com.m2i.warhammermarket.repository.TagRepository;
import com.m2i.warhammermarket.service.TagService;
import com.m2i.warhammermarket.service.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagServiceImplement implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagMapper tagMapper;

    public TagServiceImplement() {

    }

    @Override
    public TagDTO save(TagDTO tag) {
        return null;
    }

    @Override
    public List<TagDTO> findAll() {
        return this.tagRepository.findAll().stream().map(tagMapper::tagToTagDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<TagDTO> findOne(Long id) {
        return Optional.ofNullable(this.tagMapper.tagToTagDTO(this.tagRepository.findById(id).orElse(null)));
    }

    @Override
    public void delete(Long id) {

    }
}
