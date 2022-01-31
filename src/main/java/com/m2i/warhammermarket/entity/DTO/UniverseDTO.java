package com.m2i.warhammermarket.entity.DTO;

import com.m2i.warhammermarket.entity.DAO.UniverseDAO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UniverseDTO implements Serializable {

    private Long id;
    private String refCode;
    private String label;

    public UniverseDTO(UniverseDAO universeDAO) {

        this.id = universeDAO.getId();
        this.refCode = universeDAO.getRefCode();
        this.label = universeDAO.getLabel();

    }

    @Override
    public String toString() {
        return "UniverseDTO{" +
                "id=" + getId() +
                ", refCode='" + getRefCode() + "'" +
                ", label='" + getLabel() + "'" +
                '}';
    }
}
