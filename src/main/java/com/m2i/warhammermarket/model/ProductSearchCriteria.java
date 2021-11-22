package com.m2i.warhammermarket.model;


/**
 * Class for criteria product research fields
 * @since 1.1
 * @author Claire
 */
public class ProductSearchCriteria {
    private String productTag ;
    private String label;
    private int price ;
    private String univers;

    public String getProductTag() {
        return productTag;
    }

    public void setProductTag(String productTag) {
        this.productTag = productTag;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUnivers() {
        return univers;
    }

    public void setUnivers(String univers) {
        this.univers = univers;
    }
}
