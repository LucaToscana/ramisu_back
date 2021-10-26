package com.m2i.warhammermarket.entity.DAO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "Products")
public class Product {

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

    @Column (name = "id_tags")
    private int idTags;

    @Column (name = "id_universes", nullable = false)
    private int idUniverses;

    @Column (name = "id_categories", nullable = false)
    private int idCategories;
}
