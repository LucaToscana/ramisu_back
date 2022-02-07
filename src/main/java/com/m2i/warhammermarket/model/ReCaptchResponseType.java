package com.m2i.warhammermarket.model;

public class ReCaptchResponseType {  
	 
	@Override
	public String toString() {
		return "ReCaptchResponseType [success=" + success + ", challenge_ts=" + challenge_ts + ", hostname=" + hostname
				+ "]";
	}
	private boolean success;
	private String challenge_ts;
	private String hostname;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getChallenge_ts() {
		return challenge_ts;
	}
	public void setChallenge_ts(String challenge_ts) {
		this.challenge_ts = challenge_ts;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
}   