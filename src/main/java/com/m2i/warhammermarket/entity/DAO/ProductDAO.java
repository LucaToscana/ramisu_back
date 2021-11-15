package com.m2i.warhammermarket.entity.DAO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "Products")
public class ProductDAO implements Serializable {

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
    private UniverseDAO universe;

    @ManyToOne
    @JoinColumn (name = "id_categories")
    private CategoryDAO categorie;
}
