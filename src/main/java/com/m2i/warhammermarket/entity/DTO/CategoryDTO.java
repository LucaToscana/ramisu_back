package com.m2i.warhammermarket.entity.DTO;

import com.m2i.warhammermarket.entity.DAO.CategoryDAO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO implements Serializable {

    private Long id;
    private String refCode;
    private String label;

    public CategoryDTO(CategoryDAO categoryDAO) {

        this.id = categoryDAO.getId();
        this.refCode = categoryDAO.getRefCode();
        this.label = categoryDAO.getLabel();

    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "id=" + getId() +
                ", refCode='" + getRefCode() + "'" +
                ", label='" + getLabel() + "'" +
                '}';
    }
}
