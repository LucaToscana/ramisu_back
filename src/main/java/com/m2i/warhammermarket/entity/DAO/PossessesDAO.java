package com.m2i.warhammermarket.entity.DAO;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "possesses")
public class PossessesDAO implements Serializable {

	@EmbeddedId
    private PossessesId id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("id_tags")
    @JoinColumn(name = "id_tags")
    TagDAO tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("id_products")
    @JoinColumn(name = "id_products")
    ProductDAO product;
}
