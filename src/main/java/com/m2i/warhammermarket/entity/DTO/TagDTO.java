package com.m2i.warhammermarket.entity.DTO;

import com.m2i.warhammermarket.entity.DAO.TagDAO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO implements Serializable {

    private Long id;
    private String refCode;
    private String label;

    public TagDTO(TagDAO tagDAO) {

        this.id = tagDAO.getId();
        this.refCode = tagDAO.getRefCode();
        this.label = tagDAO.getLabel();

    }

    @Override
    public String toString() {
        return "TagDTO{" +
                "id=" + getId() +
                ", refCode='" + getRefCode() + "'" +
                ", label='" + getLabel() + "'" +
                '}';
    }
}