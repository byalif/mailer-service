package com.byalif.mailer.mailerapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Subscriber {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	private String email;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Subscriber() {}
	
	public Subscriber(String email) {
		this.email = email;
	};

}
