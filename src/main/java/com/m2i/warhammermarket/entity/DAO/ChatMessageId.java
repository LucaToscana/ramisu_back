package com.m2i.warhammermarket.entity.DAO;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ChatMessageId implements Serializable {
	@Column(name = "id_users")
	private Long idUser;

	
	@Column(name = "id_users")
	private Long idUserReceiver;

	@Column(name = "date")
	private 	String date;

	public ChatMessageId(Long idUser) {
		super();
		this.idUser = idUser;
	}
	
	public static final String DATE_FORMAT_NOW = "dd-MM-yyyy HH:mm:ss";

	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	public ChatMessageId(Long idUser, Long idUserReceiver) {
		super();
		this.idUser = idUser;
		this.idUserReceiver = idUserReceiver;
	}

	
}
