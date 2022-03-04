package com.m2i.warhammermarket.model;

public class ResponseMessage {
	private String content;
	private Message message;
	private TypeResponseMessage type;

	public ResponseMessage() {
	}

	public ResponseMessage(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ResponseMessage(String content, Message message, TypeResponseMessage type) {
		super();
		this.content = content;
		this.message = message;
		this.type = type;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public TypeResponseMessage getType() {
		return type;
	}

	public void setType(TypeResponseMessage type) {
		this.type = type;
	}
	
}