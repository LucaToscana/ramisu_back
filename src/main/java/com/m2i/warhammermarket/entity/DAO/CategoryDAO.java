package com.m2i.warhammermarket.entity.DAO;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


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
@Table(name = "categories")
public class CategoryDAO implements Serializable {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name ="ref_code")
    private String refCode;

    @Column (name ="label", nullable = false)
    private String label;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER , mappedBy="categorie")
    private Set<ProductDAO> products;

    /* the annotation @ToString replace it
	@Override
	public String toString() {
		return "CategoryDAO [id=" + id + ", refCode=" + refCode + ", label=" + label + "]";
	}*/

}
