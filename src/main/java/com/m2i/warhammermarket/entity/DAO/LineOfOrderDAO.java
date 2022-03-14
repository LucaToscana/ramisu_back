package com.m2i.warhammermarket.entity.DAO;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "Line_of_order")
public class LineOfOrderDAO implements Serializable {
    @EmbeddedId
    private LineOfOrderId id;

    @Column (name = "quantity", nullable = false)
    private int quantity;

    /*@Column (name = "order_price", nullable = false /*, insertable = false, updatable = false)
    private double orderTotal;*/
  
    
    
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idProduct")
    @JoinColumn(name = "id")
    ProductDAO product;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idOrder")
    @JoinColumn(name = "id_orders")
    OrderDAO order;

	@Override
	public String toString() {
		return "LineOfOrderDAO [id=" + id + ", quantity=" + quantity + /*", orderTotal=" + orderTotal +*/ "]";
	}


    
    
    
    
}
