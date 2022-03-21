package com.m2i.warhammermarket.entity.DAO;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

// Getter and setter - Lombok
@Getter
@Setter
// Constructor with and without parameters - Lombok
@NoArgsConstructor
@AllArgsConstructor
// Recover the information to String
@ToString
// Make Entity
@Entity
// Specifies the primary table for the annotated entity. (Ref: docs.oracle)
@Table(name = "universes")
public class UniverseDAO implements Serializable {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name ="ref_code")
    private String refCode;

    @Column (name ="label", nullable = false)
    private String label;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER , mappedBy="universe")
    private Set<ProductDAO> products;
}
