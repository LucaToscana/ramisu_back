package com.m2i.warhammermarket.model;


import lombok.Getter;
import lombok.Setter;

/**
 * Class for criteria product research fields
 * @since 1.1
 * @author Claire
 */
@Getter
@Setter
public class ProductSearchCriteria {
    private String productTag ;
    private String label;
    private int price;
    private String universe;
    private String category;

}
