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
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_address")
    AddressDAO address;
    
    @JsonIgnore
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
	private Set<LineOfOrderDAO> linesOfOrder;

    
    
    
    @JsonIgnore
    @OneToMany(mappedBy = "order")
	Set<NotificationDAO> notificationDAO;
    
    
    
	public OrderDAO(Date date, BigDecimal total, UsersInformationDAO user, StatusDAO status, AddressDAO address) {
		super();
		this.date = date;
		this.total = total;
		this.user = user;
		this.status = status;
		this.address = address;
	}
    
    

}