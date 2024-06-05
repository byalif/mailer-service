package com.byalif.mailer.mailerapi.DTO;

public class WeeklyDTO {
	String message;
	String subscriberEmail;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSubscriberEmail() {
		return subscriberEmail;
	}
	public void setSubscriberEmail(String subscriberEmail) {
		this.subscriberEmail = subscriberEmail;
	}
	
	public WeeklyDTO() {
		
	}
}
