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
public class InhabitId implements Serializable {
	@Column(name = "id_users")
	private Long idUser;

	@Column(name = "id_address")
	private Long idAddress;
}
