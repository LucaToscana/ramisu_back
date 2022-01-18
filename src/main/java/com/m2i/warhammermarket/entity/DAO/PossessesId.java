package com.m2i.warhammermarket.entity.DAO;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class PossessesId implements Serializable {
	@Column(name = "id_tags")
	private Long id_tags;

	@Column(name = "id_products")
	private Long id_products;
}
