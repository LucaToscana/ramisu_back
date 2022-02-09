package com.m2i.warhammermarket.entity.DAO;

import lombok.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Address")
public class AddressDAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", nullable=true)
    private String number;

    @Column(name = "street",  nullable=true)
    private String street;

    @Column(name = "additional_address" ,  nullable=true)
    private String additionalAddress;

    @Column(name = "postal_code",  nullable=true)
    private String postalCode;

    @Column(name = "city" ,  nullable=true)
    private String city;

    @Column(name = "country",  nullable=true)
    private String country;
   
    
    
    @Override
	public String toString() {
		return "AddressDAO [id=" + id + ", number=" + number + ", street=" + street + ", additionalAddress="
				+ additionalAddress + ", postalCode=" + postalCode + ", city=" + city + ", country=" + country + "]";
	}

	@JsonIgnore
    @OneToMany(mappedBy = "address")
    Set<InhabitDAO> inhabitDao;
    
    @JsonIgnore
    @OneToMany(mappedBy = "address", fetch = FetchType.EAGER)
	private Set<OrderDAO> ordersAddress;
}
