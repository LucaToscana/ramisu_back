package com.m2i.warhammermarket.entity.DAO;

import lombok.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table (name = "Products")
public class ProductDAO implements Serializable {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

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

    @Column (name ="picture")
    private String picture;

    @Column (name = "year_product")
    private String yearOfProduction;


    @ManyToOne
    @JsonIgnore
    @JoinColumn (name = "id_universes")
    private UniverseDAO universe;

    @ManyToOne
    @JoinColumn (name = "id_categories")
    private CategoryDAO categorie;
    
    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<LineOfOrderDAO> linesOfOrder;
    
    
    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PossessesDAO> possessesTagsProduct;


	@Override
	public String toString() {
		return "ProductDAO [id=" + id + ", ean13=" + ean13 + ", label=" + label + ", price=" + price + ", description="
				+ description + ", promotion=" + promotion + ", stock=" + stock + ", picture=" + picture
				+ ", yearOfProduction=" + yearOfProduction 
				+ "]";
	}
    
    
    
}
