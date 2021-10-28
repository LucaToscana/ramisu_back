package com.m2i.warhammermarket.entity.DAO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "Users")
public class User {

    @Id
    @Column (name = "id", nullable = false)
    private Long id;

    @Column (name ="mail", nullable = false)
    private String mail;

    @Column (name = "password", nullable = false)
    private String password;

    @Column (name = "date_of_creation", nullable = false)
    private Date dateOfCreation;

    @Column (name = "active", nullable = false)
    private boolean active;

    @Column (name = "token", nullable = true)
    private String token;


}
