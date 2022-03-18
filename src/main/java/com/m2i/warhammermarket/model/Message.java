package com.m2i.warhammermarket.model;

import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.m2i.warhammermarket.entity.enumeration.TypeMessage;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Message {
	private String senderName;
	private String receiverName;
	private String message;
	private String date = now();
	private TypeMessage status;
	private long idorder=0L;
	private String chat;


	public static final String DATE_FORMAT_NOW = "dd-MM-yyyy HH:mm:ss.SSS";

	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	public Message(String senderName, String receiverName, String message, TypeMessage status) {
		super();
		this.senderName = senderName;
		this.receiverName = receiverName;
		this.message = message;
		this.status = status;
		this.date =  now();
		this.idorder=0L;
		this.chat="";

	}

	
	
}
