package com.m2i.warhammermarket.service;

import com.m2i.warhammermarket.entity.DTO.TagDTO;

import java.util.List;
import java.util.Optional;

public interface TagService {

    TagDTO save (TagDTO tag);

    List<TagDTO> findAll();

    Optional<TagDTO> findOne(Long id);

    void delete(Long id);
}
