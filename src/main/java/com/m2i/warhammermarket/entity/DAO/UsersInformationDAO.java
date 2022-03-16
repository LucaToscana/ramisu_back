package com.m2i.warhammermarket.entity.DAO;

import lombok.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "Users_information")
public class UsersInformationDAO implements Serializable {
	

	@Override
	public String toString() {
		return "UsersInformationDAO [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", phone="
				+ phone + ", birthdate=" + birthdate + ", avatar=" + avatar + "]";
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "phone")
	private String phone;

	@Column(name = "birthdate")
	private Date birthdate;

	@Column(name = "avatar")
	private String avatar;

	@OneToOne
	@JoinColumn(name = "id_login")
	private UserDAO user;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	Set<InhabitDAO> inhabitDao;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	Set<NotificationDAO> notificationDAO;

	@JsonIgnore
	@OneToMany(mappedBy = "users_receiver")
	Set<ChatMessageDAO> chatMessagesDAO;

	
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	Set<ChatMessageDAO> chatMessagesSendsDAO;
}
