package com.m2i.warhammermarket.entity.DAO;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.m2i.warhammermarket.entity.enumeration.TypeMessage;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "Chats")
public class ChatsDAO implements Serializable {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_customers")
	UsersInformationDAO userCustomers;
	
	
	 @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
	 private Set<ChatMessageDAO> chatsMessage;


	public ChatsDAO(UsersInformationDAO userCustomers) {
		super();
		this.userCustomers = userCustomers;
	}


	@Override
	public String toString() {
		return "ChatsDAO [userCustomers=" + userCustomers + "]";
	}
	 
	 
	 
}
