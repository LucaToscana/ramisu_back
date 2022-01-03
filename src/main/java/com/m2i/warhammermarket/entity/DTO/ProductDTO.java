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
    private UniverseDAO universeDAO;
    private CategoryDAO categorie;


    public ProductDTO(ProductDAO productDAO) {

        this.id = productDAO.getId();
        this.ean13 = productDAO.getEan13();
        this.label = productDAO.getLabel();
        this.price = productDAO.getPrice();
        this.description = productDAO.getDescription();
        this.promotion = productDAO.getPromotion();
        this.stock = productDAO.getStock();
        this.picture = productDAO.getPicture();
        this.yearOfProduction = productDAO.getYearOfProduction();
        this.universeDAO = productDAO.getUniverseDAO();
        this.categorie = productDAO.getCategorie();

    }

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
                ", universe='" + getUniverseDAO() + "'" +
                ", id_categories='" + getCategorie() + "'" +
                "}";
    }
}
