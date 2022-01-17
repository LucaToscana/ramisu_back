package com.m2i.warhammermarket.entity.DAO;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Users_information")
public class UsersInformationDAO implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "birthdate")
    private Date birthdate;

    @OneToOne
    @JoinColumn (name ="id_login")
    private UserDAO user;
    
    
	@OneToMany(mappedBy = "user")
	Set<InhabitDAO> inhabitDao;

}
