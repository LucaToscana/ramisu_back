package com.m2i.warhammermarket.entity.DAO;

import com.nimbusds.jose.shaded.json.annotate.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Users")
public class UserDAO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;

	@Column(name = "mail", nullable = false)
	private String mail;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "date_of_creation", nullable = false)
	private Date dateOfCreation;

	@Column(name = "active", nullable = false)
	private boolean active = true;

	@Column(name = "reset_password_token", nullable = true)
	private String token;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "have", joinColumns = {
			@JoinColumn(name = "id_users", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "name_role", referencedColumnName = "name") })
	private Set<AuthorityDAO> authorities = new HashSet<>();


}
