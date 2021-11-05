package com.m2i.warhammermarket.entity.DAO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "Products")
public class Product implements Serializable {

    @Id
    @Column (name = "id", nullable = false)
    private Long id;

    @Column (name ="ean13", nullable = false)
    private String ean13;

    @Column (name ="label", nullable = false)
    private String label;

    @Column (name = "price", nullable = false)
    private BigDecimal price;

    @Column (name = "description", nullable = false)
    private String description;

    @Column (name = "promotion", nullable = false)
    private Float promotion;

    @Column (name = "stock", nullable = false)
    private int stock;

    @Column (name = "year_product")
    private String yearOfProduction;


    @ManyToOne
    @JoinColumn (name = "id_universes")
    private Universe universe;

    @ManyToOne
    @JoinColumn (name = "id_categories")
    private Category categorie;
}
