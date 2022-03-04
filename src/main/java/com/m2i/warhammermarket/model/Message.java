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

	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

}
