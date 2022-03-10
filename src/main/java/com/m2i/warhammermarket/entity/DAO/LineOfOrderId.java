package com.m2i.warhammermarket.entity.DAO;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class LineOfOrderId implements Serializable {
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "id_orders", nullable = false)
    private Long idOrder;

    @Column(name = "order_price", nullable = false)
    private BigDecimal orderTotal;
}
