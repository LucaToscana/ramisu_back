package com.m2i.warhammermarket.entity.DAO;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Universes")
public class UniverseDAO implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column (name ="ref_code")
    private String refCode;

    @Column (name ="label", nullable = false)
    private String label;
}
