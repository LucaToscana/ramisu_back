package com.m2i.warhammermarket.entity.DTO;

import com.m2i.warhammermarket.entity.DAO.Product;
import com.m2i.warhammermarket.entity.DAO.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
    private String yearOfProduction;
    private int idTags;
    private int idUniverses;
    private int idCategories;


    public ProductDTO(Product product) {

        this.id = product.getId();
        this.ean13 = product.getEan13();
        this.label = product.getLabel();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.promotion = product.getPromotion();
        this.stock = product.getStock();
        this.yearOfProduction = product.getYearOfProduction();
        this.idTags = product.getIdTags();
        this.idUniverses = product.getIdUniverses();
        this.idCategories = product.getIdCategories();
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
                ", year of production='" + getYearOfProduction() + "'" +
                ", id_tags='" + getIdTags() + "'" +
                ", id_universes='" + getIdUniverses() + "'" +
                ", id_categories='" + getIdCategories() + "'" +
                "}";
    }
}
