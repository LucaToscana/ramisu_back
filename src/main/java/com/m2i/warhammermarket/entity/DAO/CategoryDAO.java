package com.m2i.warhammermarket.entity.DAO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Categories")
public class CategoryDAO implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column (name ="ref_code")
    private String refCode;

    @Column (name ="label", nullable = false)
    private String label;

}
