package com.m2i.warhammermarket.entity.DTO;

import com.m2i.warhammermarket.entity.DAO.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO implements Serializable {

    private Long id;
    private String ean13;
    private String label;
    private BigDecimal price;
    private String description;
    private float promotion;
    private int stock;
    private String picture;
    private String yearOfProduction;
    private UniverseDAO universe; // TODO change to DTO
    private CategoryDAO categorie; // TODO change to DTO

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + getId() +
                ", ean13='" + getEan13() + "'" +
                ", label='" + getLabel() + "'" +
                ", price0'" + getPrice() + "'" +
                ", description='" + getDescription() + "'" +
                ", promotion='" + getPromotion() + "'" +
                ", stock='" + getStock() + "'" +
                ", picture='" + getPicture() + "'" +
                ", year of production='" + getYearOfProduction() + "'" +
                ", universe='" + getUniverse() + "'" +
                ", id_categories='" + getCategorie() + "'" +
                "}";
    }
}
