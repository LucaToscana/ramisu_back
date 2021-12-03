package com.m2i.warhammermarket.entity.DAO;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Line_of_order")
public class LineOfOrderDAO implements Serializable {
    @EmbeddedId
    private LineOfOrderId id;

    @Column (name = "quantity", nullable = false)
    private int quantity;
}
