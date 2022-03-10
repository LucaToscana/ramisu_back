package com.m2i.warhammermarket.entity.DAO;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Universes")
public class UniverseDAO implements Serializable {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name ="ref_code")
    private String refCode;

    @Column (name ="label", nullable = false)
    private String label;


    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER , mappedBy="Universes")
    private Set<UniverseDAO> universes;
}
