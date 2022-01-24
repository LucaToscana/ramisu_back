package com.m2i.warhammermarket.entity.DAO;

import lombok.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Orders")
public class OrderDAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "order_date", nullable = false)
    private Date date;

    @Column (name = "total", nullable = false)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn (name = "id_users")
    private UsersInformationDAO user;

    @ManyToOne
    @JoinColumn (name ="id_status")
    private StatusDAO status;
    /*
    @OneToOne(optional = false)
    @JoinColumn(unique = true)
    private AddressDAO livraisonAddress;
    
    */
    
    @JsonIgnore
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
	private Set<LineOfOrderDAO> linesOfOrder;
    
    

}