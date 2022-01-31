package com.m2i.warhammermarket.entity.wrapper;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductOrderWrapper {
    private Long id;
    private String label;
    private BigDecimal price;
    private Integer stock;
    private Integer quantite;
    private String picture;
}
