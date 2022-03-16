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

import com.m2i.warhammermarket.entity.enumeration.TypeMessage;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "ChatMessage")
public class ChatMessageDAO implements Serializable{
	@Id
	@EmbeddedId
	private ChatMessageId id;

	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("idUser")
	@JoinColumn(name = "id_users")
	UsersInformationDAO user;

	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("idUserReceiver")
	@JoinColumn(name = "users_receiver")
	UsersInformationDAO users_receiver;

	@JoinColumn(name = "message")
	String message;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "chats_ref")
	ChatsDAO chat;

	

	public ChatMessageDAO(ChatMessageId id, UsersInformationDAO user, UsersInformationDAO users_receiver,
			String message) {
		super();
		this.id = id;
		this.user = user;
		this.users_receiver = users_receiver;
		this.message = message;
	}

	@Override
	public String toString() {
		return "ChatMessageDAO [id=" + id + ", user=" + user + ", users_receiver=" + users_receiver + ", message="
				+ message + ", chat=" + chat + "]";
	}

	public LocalDate toLocalDate(String date) {

		LocalDate dateLocal = LocalDate.parse(date);
		return dateLocal;

	}
	
}
