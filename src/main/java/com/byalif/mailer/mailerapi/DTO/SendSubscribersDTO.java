package com.byalif.mailer.mailerapi.DTO;

import java.util.List;

import com.byalif.mailer.mailerapi.entity.Subscriber;

public class SendSubscribersDTO {
	WeeklyDTO weekly;
	List<Subscriber> subscribers;
	public SendSubscribersDTO(WeeklyDTO weekly, List<Subscriber> subscribers) {
		super();
		this.weekly = weekly;
		this.subscribers = subscribers;
	}
	public WeeklyDTO getWeekly() {
		return weekly;
	}
	public void setWeekly(WeeklyDTO weekly) {
		this.weekly = weekly;
	}
	public List<Subscriber> getSubscribers() {
		return subscribers;
	}
	public void setSubscribers(List<Subscriber> subscribers) {
		this.subscribers = subscribers;
	}
	
	public SendSubscribersDTO() {}
}
