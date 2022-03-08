package com.m2i.warhammermarket.model;

import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
	private StatusMessage status;
	private long idorder=0L;

	public static final String DATE_FORMAT_NOW = "dd-MM-yyyy HH:mm:ss.SSS";

	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	public Message(String senderName, String receiverName, String message, StatusMessage status) {
		super();
		this.senderName = senderName;
		this.receiverName = receiverName;
		this.message = message;
		this.status = status;
		this.date =  now();
		this.idorder=0L;
	}

	
	
}
