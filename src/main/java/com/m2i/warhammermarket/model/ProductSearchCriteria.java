package com.m2i.warhammermarket.model;

import lombok.*;

import java.util.List;

/**
 * Class for criteria product research fields
 * 
 * @since 1.1
 * @author Claire
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchCriteria {
	private String label;
	private List<String> universe;
	private int price;
	private List<String> tag;
	private List<String> category;
	private int page;
	private int pageSize;
	private float minPrice;
	private float maxPrice;;

}
