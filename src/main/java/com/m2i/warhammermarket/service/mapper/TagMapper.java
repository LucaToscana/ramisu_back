package com.m2i.warhammermarket.service.mapper;

import com.m2i.warhammermarket.entity.DAO.TagDAO;
import com.m2i.warhammermarket.entity.DTO.TagDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TagMapper {

    public List<TagDTO> tagsToTagsDTOList(List<TagDAO> tagDAOS) {
        return tagDAOS.stream()
                .filter(Objects::nonNull)
                .map(this::tagToTagDTO)
                .collect(Collectors.toList());
    }

    public TagDTO tagToTagDTO(TagDAO tagDAO) {
        return new TagDTO(tagDAO);
    }

    public List<TagDAO> tagDTOsToTags(List<TagDTO> universeDTOs) {
        return universeDTOs.stream()
                .filter(Objects::nonNull)
                .map(this::tagDTOToTag)
                .collect(Collectors.toList());
    }

    public TagDAO tagDTOToTag(TagDTO tagDTO) {
        if (tagDTO == null) {
            return null;
        } else {
            TagDAO tagDAO = new TagDAO();
            tagDAO.setId(tagDAO.getId());
            tagDAO.setRefCode(tagDAO.getRefCode());
            tagDAO.setLabel(tagDAO.getLabel());
            return tagDAO;
        }
    }

    public TagDAO tagFromId(Long id) {
        if (id == null) {
            return null;
        }
        TagDAO tagDAO = new TagDAO();
        tagDAO.setId(id);
        return tagDAO;
    }
}
